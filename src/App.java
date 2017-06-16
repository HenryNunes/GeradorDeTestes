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
		resposta.append("import org.junit.Test;\n");
		resposta.append("import static org.junit.Assert.assertEquals;\n");
		resposta.append("\n");
		
		resposta.append("public class TestJunit{\n");
		for(SequenciaDeTeste seq : sequencias)
		{
			resposta.append("\t@Test\n");
			resposta.append("\tpublic static void _" + seq.getNome() + "()\n\t{\n");
			resposta.append("\t\t--TO DO\n");
			
			List<String> temp = seq.getSequencia();
			State atual = fsm.getInitialState();
			for(int i = 0; i < temp.size(); i++)
			{
				String m = temp.get(i);
				String r = "";
				//procura nas transicoes a trancisao atual
				for(Transition tran : fsm.getTransitions())
				{
					if(tran.getSourceState().equals(atual) && tran.getInput().equals(m))
					{
						atual = tran.getTargetState();
						r = tran.getOutput();
						break;
					}
				}
				
				resposta.append("\t\tassertEquals(" + r + ", " + m + ");\n");
			}
			resposta.append("\t}\n\n");
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
