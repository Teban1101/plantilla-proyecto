package uniandes.edu.co.proyecto.dto;

import java.util.List;

public class Rfc1TransactionalResult {
    public List<Rfc1Dto> before;
    public List<Rfc1Dto> after;

    public Rfc1TransactionalResult() {}

    public Rfc1TransactionalResult(List<Rfc1Dto> before, List<Rfc1Dto> after) {
        this.before = before;
        this.after = after;
    }
}
