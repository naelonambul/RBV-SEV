package com.rbc.red.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="ASSET")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Asset {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="asset_id")
    private Long id;

    private String name;

    private long pBalance;
    private long mBalance;
    private LocalDate setDay;
    private LocalDate payDay;
    private Boolean autoPay;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="group_id")
    private Group group;

    public Asset(String name) {
        LocalDate time = LocalDate.now();
        this.name = name;
        this.pBalance = 0;
        this.mBalance = 0;
        this.setDay = time;
        this.payDay = time;
        this.autoPay = false;
    }

    public Asset(String name, long pBalance, long mBalance, LocalDate setDay, LocalDate payDay, Boolean autoPay) {
        this.name = name;
        this.pBalance = pBalance;
        this.mBalance = mBalance;
        this.setDay = setDay;
        this.payDay = payDay;
        this.autoPay = autoPay;
    }

    public Asset changeAsset(String name, long pBalance, long mBalance, LocalDate setDay, LocalDate payDay, Boolean autoPay) {
        this.name = name;
        this.pBalance = pBalance;
        this.mBalance = mBalance;
        this.setDay = setDay;
        this.payDay = payDay;
        this.autoPay = autoPay;

        return this;
    }

    public void changeGroup(Group group){
        this.group = group;
    }
}
