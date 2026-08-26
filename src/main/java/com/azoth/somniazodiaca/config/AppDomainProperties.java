package com.azoth.somniazodiaca.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.domain")
public class AppDomainProperties {

    private final Oracolo oracolo = new Oracolo();
    private final Wallet wallet = new Wallet();
    private final Premium premium = new Premium();
    private final Contenuti contenuti = new Contenuti();

    public Oracolo getOracolo() {
        return oracolo;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public Premium getPremium() {
        return premium;
    }

    public Contenuti getContenuti() {
        return contenuti;
    }

    public static class Oracolo {
        private int costoInterpretazione;
        private int costoPermanenza;
        private long durataCacheOre;
        private int limiteSognoCaratteri;
        private int limiteInterpretazioneCaratteri;
        private int costoRigenerazione;

        public int getCostoInterpretazione() {
            return costoInterpretazione;
        }

        public void setCostoInterpretazione(int costoInterpretazione) {
            this.costoInterpretazione = costoInterpretazione;
        }

        public int getCostoPermanenza() {
            return costoPermanenza;
        }

        public void setCostoPermanenza(int costoPermanenza) {
            this.costoPermanenza = costoPermanenza;
        }

        public long getDurataCacheOre() {
            return durataCacheOre;
        }

        public void setDurataCacheOre(long durataCacheOre) {
            this.durataCacheOre = durataCacheOre;
        }

        public int getLimiteSognoCaratteri() {
            return limiteSognoCaratteri;
        }

        public void setLimiteSognoCaratteri(int limiteSognoCaratteri) {
            this.limiteSognoCaratteri = limiteSognoCaratteri;
        }

        public int getLimiteInterpretazioneCaratteri() {
            return limiteInterpretazioneCaratteri;
        }

        public void setLimiteInterpretazioneCaratteri(int limiteInterpretazioneCaratteri) {
            this.limiteInterpretazioneCaratteri = limiteInterpretazioneCaratteri;
        }

        public int getCostoRigenerazione() {
            return costoRigenerazione;
        }

        public void setCostoRigenerazione(int costoRigenerazione) {
            this.costoRigenerazione = costoRigenerazione;
        }
    }

    public static class Wallet {
        private List<Integer> ricompenseGiornaliere = new ArrayList<>();
        private List<WalletPackage> pacchetti = new ArrayList<>();

        public List<Integer> getRicompenseGiornaliere() {
            return ricompenseGiornaliere;
        }

        public void setRicompenseGiornaliere(List<Integer> ricompenseGiornaliere) {
            this.ricompenseGiornaliere = ricompenseGiornaliere;
        }

        public List<WalletPackage> getPacchetti() {
            return pacchetti;
        }

        public void setPacchetti(List<WalletPackage> pacchetti) {
            this.pacchetti = pacchetti;
        }
    }

    public static class WalletPackage {
        private String codice;
        private String nome;
        private int quantitaQi;
        private int bonusQi;
        private BigDecimal prezzo;

        public String getCodice() {
            return codice;
        }

        public void setCodice(String codice) {
            this.codice = codice;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getQuantitaQi() {
            return quantitaQi;
        }

        public void setQuantitaQi(int quantitaQi) {
            this.quantitaQi = quantitaQi;
        }

        public int getBonusQi() {
            return bonusQi;
        }

        public void setBonusQi(int bonusQi) {
            this.bonusQi = bonusQi;
        }

        public BigDecimal getPrezzo() {
            return prezzo;
        }

        public void setPrezzo(BigDecimal prezzo) {
            this.prezzo = prezzo;
        }
    }

    public static class Premium {
        private BigDecimal prezzoMensile;
        private int qiMensili;
        private int interpretazioniFreeSettimana;

        public BigDecimal getPrezzoMensile() {
            return prezzoMensile;
        }

        public void setPrezzoMensile(BigDecimal prezzoMensile) {
            this.prezzoMensile = prezzoMensile;
        }

        public int getQiMensili() {
            return qiMensili;
        }

        public void setQiMensili(int qiMensili) {
            this.qiMensili = qiMensili;
        }

        public int getInterpretazioniFreeSettimana() {
            return interpretazioniFreeSettimana;
        }

        public void setInterpretazioniFreeSettimana(int interpretazioniFreeSettimana) {
            this.interpretazioniFreeSettimana = interpretazioniFreeSettimana;
        }
    }

    public static class Contenuti {
        private int limitePostCaratteri;
        private int limiteCommentoCaratteri;
        private int limitePasswordCaratteri;

        public int getLimitePostCaratteri() {
            return limitePostCaratteri;
        }

        public void setLimitePostCaratteri(int limitePostCaratteri) {
            this.limitePostCaratteri = limitePostCaratteri;
        }

        public int getLimiteCommentoCaratteri() {
            return limiteCommentoCaratteri;
        }

        public void setLimiteCommentoCaratteri(int limiteCommentoCaratteri) {
            this.limiteCommentoCaratteri = limiteCommentoCaratteri;
        }

        public int getLimitePasswordCaratteri() {
            return limitePasswordCaratteri;
        }

        public void setLimitePasswordCaratteri(int limitePasswordCaratteri) {
            this.limitePasswordCaratteri = limitePasswordCaratteri;
        }
    }
}
