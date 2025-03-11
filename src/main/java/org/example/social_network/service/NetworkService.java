package org.example.social_network.service;

import org.example.social_network.domain.*;
import org.example.social_network.domain.dto.PrietenieFilterDTO;
import org.example.social_network.repository.PagingRepository;
import org.example.social_network.repository.Repository;
import org.example.social_network.utils.HashHelper;
import org.example.social_network.utils.events.*;
import org.example.social_network.utils.observer.Observable;
import org.example.social_network.utils.observer.Observer;
import org.example.social_network.utils.paging.Page;
import org.example.social_network.utils.paging.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class NetworkService implements Observable<Event> {
    private Repository<Long, Utilizator> utilizatorRepo;
    private PagingRepository<Tuple<Long, Long>, Prietenie> prietenieRepo;
    private Repository<Tuple<Long, Long>, Cerere> cerereRepo;
    private Repository<Long, Message> messageRepo;

    private List<Observer<Event>> observers = new ArrayList<>();

    public NetworkService(Repository<Long, Utilizator> utilizatorRepo, PagingRepository<Tuple<Long, Long>, Prietenie> prietenieRepo, Repository<Tuple<Long, Long>, Cerere> cerereRepo, Repository<Long, Message> messageRepo) {
        this.utilizatorRepo = utilizatorRepo;
        this.prietenieRepo = prietenieRepo;
        this.cerereRepo = cerereRepo;
        this.messageRepo = messageRepo;
    }


    //region Gets
    public Optional<Utilizator> findUtilizator(Long id){
        return utilizatorRepo.findOne(id);
    }

    public Iterable<Utilizator> getAllUtilizator(){
        return utilizatorRepo.findAll();
    }

    public Iterable<Prietenie> getAllPrietenie(){
        return prietenieRepo.findAll();
    }

    public Page<Prietenie> getFriendshipsOfPaged(Pageable pageable, long id){
        PrietenieFilterDTO filter = new PrietenieFilterDTO();
        filter.setId(Optional.of(id));
        return prietenieRepo.findAllOnPage(pageable, filter);
    }

    public Iterable<Cerere> getAllCerere(){
        return cerereRepo.findAll();
    }

    public Optional<Message> findMessage(Long id){
        return messageRepo.findOne(id);
    }

    public Iterable<Message> getAllMessage(){
        return messageRepo.findAll();
    }

    public Iterable<Utilizator> getFriendsOf(Long id){
        ArrayList<Utilizator> friends = new ArrayList<>();
        prietenieRepo.findAll().forEach(p -> {
            Optional<Utilizator> friend = Optional.empty();
            if (p.getId().getLeft().equals(id))
                friend = utilizatorRepo.findOne(p.getId().getRight());
            else if (p.getId().getRight().equals(id))
                friend = utilizatorRepo.findOne(p.getId().getLeft());

            friend.ifPresent(friends::add);
        });
        return friends;
    }

    public Iterable<Utilizator> getNotFriendsOf(Long id){
        ArrayList<Utilizator> notFriends = (ArrayList<Utilizator>) getAllUtilizator();
        notFriends.remove(utilizatorRepo.findOne(id).get());
        prietenieRepo.findAll().forEach(p -> {
            Optional<Utilizator> friend = Optional.empty();
            if (p.getId().getLeft().equals(id))
                friend = utilizatorRepo.findOne(p.getId().getRight());
            else if (p.getId().getRight().equals(id))
                friend = utilizatorRepo.findOne(p.getId().getLeft());

            friend.ifPresent(notFriends::remove);
        });
        return notFriends;
    }

    public LocalDateTime getFriendFromUser(Long id1, Long id2) throws ServiceException{
        for(Prietenie prietenie : prietenieRepo.findAll()){
            if(prietenie.getId().getLeft().equals(id1) && prietenie.getId().getRight().equals(id2))
                return prietenie.getFriendsFrom();
            if(prietenie.getId().getLeft().equals(id2) && prietenie.getId().getRight().equals(id1))
                return prietenie.getFriendsFrom();
        }
        return null;
    }
    //endregion

    //region Utilizator
    public Optional<Utilizator> addUtilizator(String fName, String lName, String email, String password) {
        Utilizator utilizator = new Utilizator(fName, lName, email, HashHelper.getHashed(password));
        Optional<Utilizator> res = utilizatorRepo.save(utilizator);
        if(res.isPresent())
            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.ADD, utilizator));

        return res;
    }

    public void removeUtilizator(Long uId){
        Optional<Utilizator> res = utilizatorRepo.delete(uId);
        if(res.isPresent())
            notifyObservers(new UtilizatorEntityChangeEvent(ChangeEventType.DELETE, res.get()));

        getFriendsOf(uId).forEach(friend -> removePrietenie(uId, friend.getId()));
    }

    public Utilizator utilizatorLogIn(String email, String password){
        String hashed = HashHelper.getHashed(password);
        for(Utilizator utilizator : utilizatorRepo.findAll()){
            if(utilizator.authenticate(email, hashed))
                return utilizator;
        }
        return null;
    }

    //endregion

    //region Prietenie
    public void addPrietenie(Long id1, Long id2) throws ServiceException{

        if(utilizatorRepo.findOne(id1).isEmpty() || utilizatorRepo.findOne(id2).isEmpty())
            throw new ServiceException("Id-uri neexistente");

        Prietenie prietenie = new Prietenie(LocalDateTime.now());
        prietenie.setId(new Tuple<>(id1, id2));

        Optional<Prietenie> res = prietenieRepo.save(prietenie);
        if(res.isEmpty())
            notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.ADD, prietenie));
    }

    public int getFriendCount(Long id){
        if(utilizatorRepo.findOne(id).isEmpty())
            return 0;
        return (int) StreamSupport.stream(prietenieRepo.findAll().spliterator(), false)
                .filter(p -> p.getId().getLeft().equals(id) || p.getId().getRight().equals(id))
                .count();
    }

    public void removePrietenie(Long id1, Long id2) throws ServiceException{
        if(prietenieRepo.findOne(new Tuple<>(id1, id2)).isEmpty())
            throw new ServiceException("Prietenie neexistenta");


        Optional<Prietenie> res = prietenieRepo.delete(new Tuple<>(id1, id2));
        if(res.isPresent())
            notifyObservers(new PrietenieEntityChangeEvent(ChangeEventType.DELETE, res.get()));

        deleteCerere(id1, id2);
    }
    //endregion

    //region Cerere
    public void sendCerere(Long id1, Long id2) throws ServiceException{
        if(utilizatorRepo.findOne(id1).isEmpty() || utilizatorRepo.findOne(id2).isEmpty())
            throw new ServiceException("Id-uri neexistente");

        Cerere cerere = new Cerere(LocalDateTime.now(), "Pending");
        cerere.setId(new Tuple<>(id1, id2));
        Optional<Cerere> res = cerereRepo.save(cerere);
        if(res.isEmpty())
            notifyObservers(new CerereEntityChangeEvent(ChangeEventType.ADD, cerere));
    }

    public void acceptCerere(Long id1, Long id2) throws ServiceException{
        if(utilizatorRepo.findOne(id1).isEmpty() || utilizatorRepo.findOne(id2).isEmpty())
            throw new ServiceException("Id-uri neexistente");
        Optional<Cerere> cerere = cerereRepo.findOne(new Tuple<>(id1, id2));

        if(cerere.isEmpty())
            throw new ServiceException("Cerere inexistenta");

        if(!cerere.get().getStatus().equals("Pending"))
            throw new ServiceException("Cerere deja procesata");

        Cerere noua = new Cerere(cerere.get().getTimeSent(), "Accepted");
        noua.setId(new Tuple<>(id1, id2));

        Optional<Cerere> res = cerereRepo.update(noua);

        if(res.isEmpty())
            notifyObservers(new CerereEntityChangeEvent(ChangeEventType.UPDATE, noua));

        addPrietenie(id1, id2);
    }

    public void rejectCerere(Long id1, Long id2) throws ServiceException{
        if(utilizatorRepo.findOne(id1).isEmpty() || utilizatorRepo.findOne(id2).isEmpty())
            throw new ServiceException("Id-uri neexistente");

        Optional<Cerere> cerere = cerereRepo.findOne(new Tuple<>(id1, id2));

        if(cerere.isEmpty())
            throw new ServiceException("Cerere inexistenta");

        if(!cerere.get().getStatus().equals("Pending"))
            throw new ServiceException("Cerere deja procesata");

        Cerere noua = new Cerere(cerere.get().getTimeSent(), "Rejected");
        noua.setId(new Tuple<>(id1, id2));

        Optional<Cerere> res = cerereRepo.update(noua);

        if(res.isEmpty())
            notifyObservers(new CerereEntityChangeEvent(ChangeEventType.UPDATE, noua));

    }

    public void deleteCerere(Long id1, Long id2) throws ServiceException{
        if(utilizatorRepo.findOne(id1).isEmpty() || utilizatorRepo.findOne(id2).isEmpty())
            throw new ServiceException("Id-uri neexistente");

        Optional<Cerere> res = cerereRepo.delete(new Tuple<>(id1, id2));
        if(res.isPresent())
            notifyObservers(new CerereEntityChangeEvent(ChangeEventType.DELETE, res.get()));
    }
    //endregion

    //region Message

    public void sendMessage(Long fromId, List<Long> toIdList, String text, LocalDateTime dateTime, Long reply_to) throws ServiceException{
        Message message = new Message(fromId, toIdList, text, dateTime, reply_to);
        Optional<Message> res = messageRepo.save(message);
        if(res.isEmpty())
            notifyObservers(new MessageEntityChangeEvent(ChangeEventType.ADD, message));
    }


    //endregion

    //region Community
    public int getCommunityCount(){
        CommunityGraph graph = new CommunityGraph(utilizatorRepo.findAll(), prietenieRepo.findAll());
        return graph.getCommunityCount();
    }

    public ArrayList<Long> getMostSocial(){
        CommunityGraph graph = new CommunityGraph(utilizatorRepo.findAll(), prietenieRepo.findAll());
        return graph.getMostSocial();
    }
    //endregion

    //region Observer

    @Override
    public void addObserver(Observer<Event> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<Event> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(Event t) {

        observers.forEach(x->x.update(t));
    }

    //endregion
}
