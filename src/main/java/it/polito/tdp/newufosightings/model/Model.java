package it.polito.tdp.newufosightings.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	private NewUfoSightingsDAO dao;
	private Map<String, State> idMap;
	private Graph<State, DefaultWeightedEdge> grafo;
	private List<Sighting> avvistamenti;
	private Simulator s;
	
	public Model() {
		dao = new NewUfoSightingsDAO();
		idMap = new HashMap<>();
		dao.loadAllStates(idMap);
		grafo = null;
	}
	
	public List<String> getFormeAnno(Integer anno){
		return dao.listFormePerAnno(anno);
	}
	
	public void creaGrafo(Integer anno, String forma) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, idMap.values());
		for(CoppiaStati c : dao.listCoppieStati(anno, forma, idMap))
			Graphs.addEdge(grafo, c.getS1(), c.getS2(), c.getPeso());
		avvistamenti = dao.listAvvistamentiPerAnnoEForma(anno, forma, idMap);
	}
	
	public Graph<State, DefaultWeightedEdge> getGrafo(){
		return grafo;
	}

	public Integer sommaPesiArchi(State s) {
		Integer somma = 0;
		for(DefaultWeightedEdge e : grafo.edgesOf(s))
			somma += (int)grafo.getEdgeWeight(e);
		return somma;
	}

	public List<Sighting> getAvvistamenti() {
		return avvistamenti;
	}
	
	public void simula(Integer t1, Integer alfa) {
		s = new Simulator();
		s.init(this, t1, alfa);
		s.run();
	}
}
