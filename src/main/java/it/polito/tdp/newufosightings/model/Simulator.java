package it.polito.tdp.newufosightings.model;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.newufosightings.model.Event.EventType;

public class Simulator {

	//PARAMETRI 
	//private Model model;
	private Graph<State, DefaultWeightedEdge> grafo;
	private List<Sighting> avvistamenti;
	private Integer t1;
	private Integer alfa;
	
	//OUTPUT
	//STATO DEL SISTEMA
	//--> il defcon di ogno stato
	
	//CODA EVENTI
	private Queue<Event> queue;
	
	public void init(Model model, Integer t1, Integer alfa) {
		//this.model = model;
		this.grafo = model.getGrafo();
		this.avvistamenti = model.getAvvistamenti();
		this.t1 = t1;
		this.alfa = alfa;
		queue = new PriorityQueue<>();
		for(Sighting s : avvistamenti) 
			queue.add(new Event(s.getDatetime(), EventType.AVVISTAMENTO, s, s.getState()));
	}
	
	public void run() {
		while(!queue.isEmpty()) {
			Event e = queue.poll();
			System.out.println(e);
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch (e.getType()) {
			case AVVISTAMENTO:
				e.getStato().setDefcon(e.getStato().getDefcon()-1);
				//if(e.getDatetime().plus(t1, ChronoUnit.DAYS).getYear()==e.getDatetime().getYear())
				//-->riga per interrompere gli eventi a fine anno (per evitare che tutti i defcon tornino a 5 nell'anno successivo)
					queue.add(new Event(e.getDatetime().plus(t1, ChronoUnit.DAYS), EventType.CESSATA_ALLERTA_PRINCIPALE, e.getAvvistamento(), e.getStato()));
				Double random = Math.random()*100;
				if(random<alfa)
					for(State s : Graphs.neighborListOf(grafo, e.getStato())) {
						s.setDefcon(s.getDefcon()-0.5);
						//if(e.getDatetime().plus(t1, ChronoUnit.DAYS).getYear()==e.getDatetime().getYear())
						//-->riga per interrompere gli eventi a fine anno (per evitare che tutti i defcon tornino a 5 nell'anno successivo)
							queue.add(new Event(e.getDatetime().plus(t1, ChronoUnit.DAYS), EventType.CESSATA_ALLERTA_VICINO, e.getAvvistamento(), s));
					}
				break;
			case CESSATA_ALLERTA_PRINCIPALE:
				if(e.getStato().getDefcon()<=4)
					e.getStato().setDefcon(e.getStato().getDefcon()+1);
				break;
			case CESSATA_ALLERTA_VICINO:
				if(e.getStato().getDefcon()<=4.5)
					e.getStato().setDefcon(e.getStato().getDefcon()+0.5);
				break;
		}
		
	}
	
}
