import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import finiteStateMachine.FiniteStateMachine;
import finiteStateMachine.State;
import finiteStateMachine.Transition;
import utilities.DomParser;

public class App {
	
	private static FiniteStateMachine fsm;
	
	public static void main(String[] args) 
	{
		List<SequenciaDeTeste> sequencias = leitor(args);
		String teste = transformar(sequencias);
		salvar(teste, args);
		System.exit(0);
	}

	public static List<SequenciaDeTeste> leitor(String[] args)
	{
		if(args.length != 2)
		{
			System.err.println("Enviar o primeiro argumento com  arquivo que contem as sequencias de teste e o segundo com a maquina de estados no formato .jff");
			System.exit(1);
		}
		File arq = new File(args[0]);				
		Scanner in = null;
		
		try
		{
			in = new Scanner(arq);
		}
		catch (Exception e) 
		{
			System.err.println("Erro na leitura do arquivo das sequencias: " + args[0]);
			System.exit(1);
		}		
		List<SequenciaDeTeste> resposta = new ArrayList<SequenciaDeTeste>();
		
		//Percorre até chegar nas sequencias de teste
		while(in.hasNext())
		{
			String linha = in.nextLine();
			if(linha.contains("Sequ?ncias de Teste:"))break;
		}
		while(in.hasNext())
		{
			resposta.add(new SequenciaDeTeste(in.nextLine()));
		}
		in.close();
		
		//Leitura fa Maquina de estados
		fsm = new FiniteStateMachine();
		DomParser.parse(fsm, args[1]);
		
		return resposta;
	}
	public static String transformar(List<SequenciaDeTeste> sequencias)
	{
		StringBuilder resposta = new StringBuilder();
		resposta.append("import org.junit.Test;\r\n");
		resposta.append("import static org.junit.Assert.assertEquals;\r\n");
		resposta.append("\n");
		
		resposta.append("public class TestJunit{\r\n");
		resposta.append("\t@Before\r\n");
		resposta.append("\tpublic static void _start()\r\n\t{\r\n");
		resposta.append("\t\t//--TO DO\r\n");
		resposta.append("\t}\r\n");
		
		for(SequenciaDeTeste seq : sequencias)
		{
			resposta.append("\t@Test\r\n");
			resposta.append("\tpublic static void _" + seq.getNome() + "()\r\n\t{\r\n");
			resposta.append("\t\t//--TO DO\r\n");
			
			List<String> temp = seq.getSequencia();
			State atual = fsm.getInitialState();
			for(int i = 0; i < temp.size(); i++)
			{
				String m = temp.get(i);
				
				if(i == temp.size()-1)
				{
					//System.out.println("Erro trans " + seq.getNome() + ": " + m);
					//System.out.println(temp);
				}
				String r = "";
				//procura nas transicoes a trancisao atual
				for(Transition tran : fsm.getTransitions())
				{
					if(tran.getSourceState().equals(atual) && tran.getInput().equals(m))
					{
						atual = tran.getTargetState();
						r = tran.getOutput();
						if(r.equals(""))
						{
							//System.out.println("Erro valor: " + r);
						}
						break;
					}
				}
				
				if(!r.equals(FiniteStateMachine.EPSILON))
				{
					
					resposta.append("\t\tassertEquals(" + r + ", " + m + ");\r\n");
				}
				else
				{
					resposta.append("\t\t" + m + ";\r\n");
				}
			}
			resposta.append("\t}\r\n\r\n");
		}
		resposta.append("}");
		return resposta.toString();
	}
	public static void salvar(String teste, String[] args)
	{
		//System.err.println(args[0].substring(0, args[0].indexOf('.')));
		String nome = args[0];
		if(nome.indexOf('.') >= 0)
		{
			nome = nome.substring(0, nome.indexOf('.'));
		}
		nome = nome + ".java";
		
		
		Writer out  = null;
		try{
			out = new FileWriter(nome);
			out.write(teste);
			out.flush();
			out.close();
		}
		catch (Exception e) {
			System.err.println("Erro na escrita do arquivo");
			System.exit(1);
		}		
		System.out.println("Transformação completa");
	}
}
