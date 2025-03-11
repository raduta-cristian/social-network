package org.example.social_network.repository;

public class RepositoryException extends RuntimeException {
    public RepositoryException() {
    }
  
    public RepositoryException(String message) {
      super("RepositoryException: " + message);
    }
  
    public RepositoryException(String message, Throwable cause) {
      super("RepositoryException: " + message, cause);
    }
  
    public RepositoryException(Throwable cause) {
      super(cause);
    }
  
    public RepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super("RepositoryException: " + message, cause, enableSuppression, writableStackTrace);
    }
}
