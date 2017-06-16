import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

	public static void main(String[] args) 
	{
		List<SequenciaDeTeste> sequencias = leitor(args);
		String teste = transformar(sequencias);
		salvar(teste, args);
		System.exit(0);
	}

	public static List<SequenciaDeTeste> leitor(String[] args)
	{
		if(args.length != 1)
		{
			System.err.println("Enviar um argumento com nome do arquivo que contem as sequencias de teste");
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
			System.err.println("Erro na leitura do arquivo: " + args[0]);
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
			for(String s : seq.getSequencia())
			{
				resposta.append("\t\tassertEquals(" + "true, " + s + ");\n");
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
