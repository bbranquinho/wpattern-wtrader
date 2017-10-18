package org.wtrader.test.cep.data.repositories;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.wtrader.cep.data.repositories.IStockRecordRepository;
import org.wtrader.cep.data.repositories.IStockRepository;
import org.wtrader.cep.utils.data.entities.StockEntity;
import org.wtrader.cep.utils.data.entities.StockRecordEntity;
import org.wtrader.test.cep.data.utils.AbstractCepDatabaseTest;

public class StockRepositoryTest extends AbstractCepDatabaseTest {

	private Logger logger = Logger.getLogger(this.getClass());

	@Inject
	private IStockRepository stockRepository;

	@Inject
	private IStockRecordRepository stockRecordRepository;

	private String[] stockies = new String[] {
			"BRAP4",
			"PETR3",
			"PETR4",
			"DIRR3",
			"CCXC3",
			"EQTL3",
			"TAEE11",
			"MYPK3",
			"BBSE3",
			"JHSF3",
			"LLIS3",
			"CPFE3",
			"ODPV3",
			"CIEL3",
			"BRFS3",
			"CYRE3",
			"ELET6",
			"GGBR3",
			"MDIA3",
			"BBRK3",
			"BISA3",
			"GGBR4",
			"WEGE3",
			"BRML3",
			"GRND3",
			"HYPE3",
			"ELET3",
			"VVAR3",
			"MMXM3",
			"CSMG3",
			"VLID3",
			"ALPA4",
			"RSID3",
			"OIBR4",
			"OIBR3",
			"BRSR6",
			"LAME4",
			"LAME3",
			"EZTC3",
			"BPNM4",
			"TCSA3",
			"SANB11",
			"MPXE3",
			"PMAM3",
			"NATU3",
			"PCAR5",
			"MILS3",
			"PCAR4",
			"LEVE3",
			"LPSB3",
			"KLBN4",
			"RAPT4",
			"TRPL4",
			"RENT3",
			"TIMP3",
			"HGTX3",
			"STBP11",
			"TBLE3",
			"BEEF3",
			"ELPL4",
			"IMCH3",
			"LIGT3",
			"CMIG3",
			"BTOW3",
			"ESTC3",
			"CMIG4",
			"BVMF3",
			"UGPA3",
			"CCRO3",
			"MAGG3",
			"CSNA3",
			"SSBR3",
			"ECOR3",
			"CPLE6",
			"BBAS3",
			"AMBV4",
			"SUZB5",
			"SBSP3",
			"RDCD3",
			"ABCB4",
			"ENBR3",
			"USIM5",
			"MRVE3",
			"USIM3",
			"OGXP3",
			"GFSA3",
			"QUAL3",
			"AMAR3",
			"QGEP3",
			"MGLU3",
			"CSAN3",
			"BPHA3",
			"SLCE3",
			"JBSS3",
			"COCE5",
			"MULT3",
			"VIVT4",
			"HBOR3",
			"GOAU4",
			"MPLU3",
			"BBDC3",
			"CESP6",
			"BBDC4",
			"LLXL3",
			"BRIN3",
			"KROT3",
			"VALE5",
			"IGTA3",
			"AUTM3",
			"VALE3",
			"BRKM5",
			"GOLL4",
			"RADL3",
			"CRUZ3",
			"GETI4",
			"EMBR3",
			"GETI3",
			"CTIP3",
			"SMTO3",
			"ARTR3",
			"VAGR3",
			"ALLL3",
			"PDGR3",
			"HRTP3",
			"JSLG3",
			"TGMA3",
			"ITSA4",
			"DTEX3",
			"ITUB3",
			"LREN3",
			"MRFG3",
			"ITUB4",
			"ALSC3",
			"FLRY3",
			"AEDU3",
			"FIBR3",
			"SULA11",
			"DASA3",
			"ECOD3",
			"EVEN3",
			"TOTS3",
			"ARZZ3",
			"POMO4",
			"PSSA3",
			"BRPR3",
			"ABRE11"
	};

	@Test
	public void testStockDateRepository() {
		List<StockEntity> stockNames = this.stockRepository.findAll();

		if (this.logger.isDebugEnabled()) {
			this.logger.debug(stockNames);
		}
	}

	@Test
	public void testCountCompanies() {
		int count = 0;

		for (String stockName : this.stockies) {
			if (this.stockRepository.findByName(stockName) != null) {
				count++;
			} else {
				this.logger.info("Stock not founded " + stockName);
			}
		}

		this.logger.info("Count " + count + " of " + this.stockies.length);
	}

	@Test
	public void testCountCompaniesLastDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		StockEntity stock;
		Date start = sdf.parse("28-12-2012");
		Date end = sdf.parse("28-12-2012");;
		int count = 0;
		List<StockRecordEntity> stocks;

		for (String stockName : this.stockies) {
			stock = this.stockRepository.findByName(stockName);

			if (stock != null) {
				stocks = this.stockRecordRepository.findByTradeDateBetweenAndStock(start, end, stock);

				if (stocks != null && !stocks.isEmpty()) {
					count++;
				} else {
					this.logger.info("Date not founded " + stockName);
				}

			} else {
				this.logger.info("Stock not founded " + stockName);
			}
		}

		this.logger.info("Count " + count + " of " + this.stockies.length);
	}

	@Test
	public void testFindByLowestTradeDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date lowestTradeDate = sdf.parse("01-01-2004");

		List<String> stocks = this.stockRepository.findByTradeDateAndEmptyRecordsPercentual(0.95f, lowestTradeDate);

		if (this.logger.isInfoEnabled()) {
			this.logger.info(String.format("Founded [%s] records.", stocks.size()));
			this.logger.info(stocks);
		}
	}

}
