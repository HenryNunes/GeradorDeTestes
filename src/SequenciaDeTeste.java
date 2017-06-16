import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SequenciaDeTeste {
	private String nome;
	private List<String> sequencia;
	
	public SequenciaDeTeste(String sequencia)
	{
		this.nome = sequencia.substring(1, sequencia.indexOf(':')-1).replace(' ', '_');
		this.sequencia = new ArrayList<String>();
		
		String sequenciaBruta = sequencia.substring(sequencia.indexOf('[')+1, sequencia.indexOf(']'));		
		String[] sequenciaRefinada = sequenciaBruta.split(", ");
		Collections.addAll(this.getSequencia(), sequenciaRefinada);
	}
	String getNome() {
		return nome;
	}
	List<String> getSequencia() {
		return sequencia;
	}
	String getElementoSequencia(int posicao)
	{
		if(posicao > this.sequencia.size() - 1)return null;
		return this.sequencia.get(posicao);
	}
	
}
