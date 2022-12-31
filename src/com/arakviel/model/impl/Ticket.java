package com.arakviel.model.impl;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import com.arakviel.model.Model;

/**
 * Головна модель бізнес логіки "Квиток".
 */
public class Ticket implements Model {

    private UUID id;
    private Client client;
    private Train train;
    private String trainCarriage;
    private Station from;
    private Station to;
    private LocalDateTime departure;
    private LocalDateTime arrival;
    private Money money;
    private Seat seat;

    private Ticket(UUID id, Client client, Train train, String trainCarriage, Station from,
            Station to, LocalDateTime departure, LocalDateTime arrival, Money money, Seat seat) {
        this.id = id;
        this.client = client;
        this.train = train;
        this.trainCarriage = trainCarriage;
        this.from = from;
        this.to = to;
        this.departure = departure;
        this.arrival = arrival;
        this.money = money;
        this.seat = seat;
    }

    public static TicketBuilderId builder() {
        return id -> client -> train -> trainCarriage -> from -> to -> departure -> arrival -> money -> seat -> () -> new Ticket(
                id, client, train, trainCarriage, from, to, departure, arrival, money, seat);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public String getTrainCarriage() {
        return trainCarriage;
    }

    public void setTrainCarriage(String trainCarriage) {
        this.trainCarriage = trainCarriage;
    }

    public Station getFrom() {
        return from;
    }

    public void setFrom(Station from) {
        this.from = from;
    }

    public Station getTo() {
        return to;
    }

    public void setTo(Station to) {
        this.to = to;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDateTime departure) {
        this.departure = departure;
    }

    public LocalDateTime getArrival() {
        return arrival;
    }

    public void setArrival(LocalDateTime arrival) {
        this.arrival = arrival;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public interface TicketBuilderId {

        TicketBuilderClient id(UUID id);
    }

    public interface TicketBuilderClient {

        TicketBuilderTrain client(Client client);
    }

    public interface TicketBuilderTrain {

        TicketBuilderTrainCarriage train(Train train);
    }

    public interface TicketBuilderTrainCarriage {

        TicketBuilderFrom trainCarriage(String trainCarriage);
    }

    public interface TicketBuilderFrom {

        TicketBuilderTo from(Station from);
    }

    public interface TicketBuilderTo {

        TicketBuilderDeparture to(Station to);
    }

    public interface TicketBuilderDeparture {

        TicketBuilderArrival departure(LocalDateTime departure);
    }

    public interface TicketBuilderArrival {

        TicketBuilderMoney arrival(LocalDateTime arrival);
    }

    public interface TicketBuilderMoney {

        TicketBuilderSeat money(Money money);
    }

    public interface TicketBuilderSeat {

        TicketBuilder seat(Seat seat);
    }

    public interface TicketBuilder {

        Ticket build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id);
        sb.append(", пасажир=").append(client);
        sb.append(", потяг=").append(train);
        sb.append(", вагон потяга='").append(trainCarriage).append('\'');
        sb.append(", звідки=").append(from);
        sb.append(", куди=").append(to);
        sb.append(", час відправки=").append(departure);
        sb.append(", час прибуття=").append(arrival);
        sb.append(", ціна=").append(money);
        sb.append(", місце=").append(seat);
        sb.append('.');
        return sb.toString();
    }
}
