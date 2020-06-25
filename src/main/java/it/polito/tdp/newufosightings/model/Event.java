package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;

public class Event implements Comparable<Event> {

	public enum EventType {
		AVVISTAMENTO, CESSATA_ALLERTA_PRINCIPALE, CESSATA_ALLERTA_VICINO;
	}
	
	private LocalDateTime datetime;
	private EventType type;
	private Sighting avvistamento;
	private State stato;

	public Event(LocalDateTime datetime, EventType type, Sighting avvistamento, State stato) {
		super();
		this.datetime = datetime;
		this.type = type;
		this.avvistamento = avvistamento;
		this.stato = stato;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Sighting getAvvistamento() {
		return avvistamento;
	}

	public void setAvvistamento(Sighting avvistamento) {
		this.avvistamento = avvistamento;
	}

	public State getStato() {
		return stato;
	}

	public void setStato(State stato) {
		this.stato = stato;
	}
	

	@Override
	public String toString() {
		return "Event [datetime=" + datetime + 
				", type=" + type + 
				", avvistamento=" + avvistamento.getId() + 
				", stato=" + stato.getName()
				+ "]";
	}

	@Override
	public int compareTo(Event o) {
		return this.datetime.compareTo(o.datetime);
	}
	
	
}
