package br.com.caelum.leilao.dominio;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class LeilaoTest {

	private Usuario steveJobs;
	private Usuario billGates;
	private Usuario steveWozniak;

	@Before
	public void criaUsuario() {
		this.steveJobs = new Usuario("Steve Jobs");
		this.billGates = new Usuario("Bill Gates");
		this.steveWozniak = new Usuario("Steve Wozniak");
	}
	

	@Test
	public void deveReceberApenasUmLance() {
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15").constroi();
		assertEquals(0, leilao.getLances().size());

		leilao.propoe(new Lance(steveWozniak, 2000));

		assertEquals(1, leilao.getLances().size());
		assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
	}

	@Test
	public void deveReceberVariosLances() {
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
							.lance(steveJobs, 2000)
							.lance(steveWozniak, 3000)
							.constroi();

		assertEquals(2, leilao.getLances().size());
		assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
		assertEquals(3000.0, leilao.getLances().get(1).getValor(), 0.00001);
	}

	@Test
	public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario() {
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
							.lance(steveJobs, 2000)
							.lance(steveJobs, 3000)
							.constroi();

		assertEquals(1, leilao.getLances().size());
		assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
	}

	@Test
	public void naoDeveAceitarMaisDoQue5LancesDoMesmoUsuario() {
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
							.lance(steveJobs, 1000).lance(billGates, 1500)
							.lance(steveJobs, 2000).lance(billGates, 2500)
							.lance(steveJobs, 3000).lance(billGates, 3500)
							.lance(steveJobs, 4000).lance(billGates, 4500)
							.lance(steveJobs, 5000).lance(billGates, 5500)
							.lance(steveJobs, 6500)
							.constroi();

		assertEquals(10, leilao.getLances().size());
		assertEquals(5500.0,
				leilao.getLances().get(leilao.getLances().size() - 1)
						.getValor(), 0.00001);

	}

	@Test
	public void deveDobrarOUltimoLanceDado() {
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
							.lance(steveJobs, 2000)
							.lance(billGates, 3000)
							.constroi();

		leilao.dobraLance(steveJobs);

		assertEquals(4000, leilao.getLances().get(2).getValor(), 0.00001);
	}

	@Test
	public void naoDeveDobrarCasoNaoHajaLanceAnterior() {
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15").constroi();		

		leilao.dobraLance(steveJobs);

		assertEquals(0, leilao.getLances().size());
	}

}
