package org.example.social_network.domain;

import java.util.*;

public class CommunityGraph {
    Map<Long, ArrayList<Long>> adiacente;

    public CommunityGraph(Iterable<Utilizator> utilizatori,Iterable<Prietenie> prietenii){
        adiacente = new HashMap<>();

        utilizatori.forEach(u -> adiacente.put(u.getId(), new ArrayList<>()));

        prietenii.forEach(p -> {
            adiacente.get(p.getId().getLeft()).add(p.getId().getRight());
            adiacente.get(p.getId().getRight()).add(p.getId().getLeft());
        });
    }

    private void dfs(Long v, ArrayList<Long> group, Set<Long> seen){
        seen.add(v);
        group.add(v);
        adiacente.get(v).stream().filter(u -> !seen.contains(u)).forEach(u -> dfs(u, group, seen));
    }


    private ArrayList<ArrayList<Long>> getCommunities(){
        ArrayList<ArrayList<Long>> result = new ArrayList<>();
        Set<Long> seen = new HashSet<Long>();

        adiacente.entrySet().stream().filter(p -> !seen.contains(p.getKey())).forEach(p -> {
            ArrayList<Long> group = new ArrayList<>();
            dfs(p.getKey(), group, seen);
            result.add(group);
        });
        return result;
    }


    public int getCommunityCount(){
        return getCommunities().size();
    }

    private int dfsCount(Long v, Set<Long> seen, Set<Long> exhausted){
        seen.add(v);
        int maxLen = 0;
        for(Long u : adiacente.get(v))
            if(!seen.contains(u) && !exhausted.contains(u)){
                int len = 1 + dfsCount(u, seen, exhausted);
                if(len > maxLen)
                    maxLen = len;
            }
        seen.remove(v);
        exhausted.add(v);
        return maxLen;
    }

    private int bfsMax(Long v){
        int maxLen = 0;
        ArrayList<Long> q = new ArrayList<>();
        ArrayList<Integer> len = new ArrayList<>();
        Set<Long> seen = new HashSet<>();
        q.add(v);
        len.add(0);
        seen.add(v);
        while(!q.isEmpty()){
            Long u = q.removeFirst();
            int l = len.removeFirst();
            for(Long p : adiacente.get(u)){
                if(!seen.contains(p)){
                    q.add(p);
                    len.add(l+1);
                    seen.add(p);

                    if(maxLen < l+1)
                        maxLen = l+1;
                }
            }
        }

        return maxLen;
    }

    private int getLongestPathLenght(ArrayList<Long> group){
        int maxLen = 0;
        for(Long v : group){
//            int len = dfsCount(v, new HashSet<>(), new HashSet<>());
            int len = bfsMax(v);
            if(len > maxLen)
                maxLen = len;
        }
        return maxLen;
    }

    public ArrayList<Long> getMostSocial(){
        ArrayList<Long> result = new ArrayList<Long>();
        int maxLength = 0;
        for(ArrayList<Long> group : getCommunities()){
            int pathLength = getLongestPathLenght(group);
            if(pathLength > maxLength || maxLength == 0){
                maxLength = pathLength;
                result = group;
            }
        }
        System.out.println(maxLength);
        return result;
    }
}
