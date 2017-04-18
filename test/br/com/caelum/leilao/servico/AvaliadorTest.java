package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.servico.Avaliador;

public class AvaliadorTest {

	private Avaliador leiloeiro;
	private Usuario maria;
	private Usuario jose;
	private Usuario joao;

	@Before
	public void criaAvaliador() {
		this.leiloeiro = new Avaliador();
		this.joao = new Usuario("Joao");
		this.jose = new Usuario("Jose");
		this.maria = new Usuario("Maria");
	}
	
	@Test(expected=RuntimeException.class)
	public void naoDeveAvaliarLeiloesSemNenhumLanceDado() {
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15").constroi();
		leiloeiro.avalia(leilao);
		
	}	

	@Test
	public void deveEntenderLancesEmOrdemCrescente() {
		Leilao leilao = new CriadorDeLeilao().para("PlayStation 3 Novo")
				.lance(joao, 500.00)
				.lance(jose, 700.00)				
				.lance(maria, 200.00)
				.constroi();
		
		leiloeiro.avalia(leilao);

		assertThat(leiloeiro.getMenorLance(), equalTo(200.0));
		assertThat(leiloeiro.getMaiorLance(), equalTo(700.0));		
	}

	@Test
	public void deveEntenderLeilaoComApenasUmLance() {
		Leilao leilao = new CriadorDeLeilao().para("PlayStation 3 Novo")
				.lance(joao, 1000.00)
				.constroi();

		leiloeiro.avalia(leilao);

		double maiorLance = 1000.0;
		double menorLance = 1000.0;

		assertEquals(maiorLance, leiloeiro.getMaiorLance(), 0.00001);
		assertEquals(menorLance, leiloeiro.getMenorLance(), 0.00001);
	}

	@Test
	public void deveEntenderLancesRandomicos() {
		Leilao leilao = new CriadorDeLeilao().para("PlayStation 3 Novo")
				.lance(joao, 1000.00)
				.lance(maria, 3000.00)				
				.lance(joao, 500.00)				
				.lance(maria, 2100.00)
				.lance(joao, 1990.00)				
				.constroi();		
		
		leiloeiro.avalia(leilao);

		double menorLance = 500.0;
		double maiorLance = 3000.0;

		assertEquals(menorLance, leiloeiro.getMenorLance(), 0.00001);
		assertEquals(maiorLance, leiloeiro.getMaiorLance(), 0.00001);

	}

	@Test
	public void deveEncontrarOsTresPrimeirosLances() {
		Leilao leilao = new CriadorDeLeilao().para("PlayStation 3 Novo")
				.lance(joao, 1000.00)
				.lance(maria, 2000.00)				
				.lance(joao, 3000.00)				
				.lance(maria, 1500.00)
				.constroi();

		leiloeiro.avalia(leilao);

		List<Lance> maiores = leiloeiro.getTresMaiores();

		assertEquals(3, maiores.size());
		
		// JUNIT
		// assertEquals(3000.0, maiores.get(0).getValor(), 0.00001);
		// assertEquals(2000.0, maiores.get(1).getValor(), 0.00001);
		// assertEquals(1500.0, maiores.get(2).getValor(), 0.00001);
		
		assertThat(maiores, hasItems(
					new Lance(joao, 3000.0),
					new Lance(maria, 2000.0),
					new Lance(maria, 1500.0)				
				));

	}

}
