package com.technoserv.bio.kernel.model.objectmodel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CONVOLUTIONS")
public class Convolution extends AbstractObject {
    /**
     * сам бинарный вектор свертки
     */
    @Column
    @Lob
    private byte[] convolution;
    /**
     * версия нейронной сети, с помощью которой свертка получена
     */
    @Column(name = "CNN_VERSION")
    private int cnnVersion;
    /**
     * список ссылок на Стоп листов, в которые входит эта свертка
     */
    @OneToMany
    private List<StopList> stopListEntries;

    public byte[] getConvolution() {
        return convolution;
    }

    public void setConvolution(byte[] convolution) {
        this.convolution = convolution;
    }

    public int getCnnVersion() {
        return cnnVersion;
    }

    public void setCnnVersion(int cnnVersion) {
        this.cnnVersion = cnnVersion;
    }

    public List<StopList> getStopListEntries() {
        return stopListEntries;
    }

    public void setStopListEntries(List<StopList> stopListEntries) {
        this.stopListEntries = stopListEntries;
    }
}