package pl.codeleak.demos.sbt.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.codeleak.demos.sbt.model.Venta;

public interface VentasRepository extends CrudRepository<Venta, Integer> {

    @Query("from Venta v where v.clienteCuentaCorriente is not null and v.pagoCuentaCorriente is not null and v.fechaCancelacionCuentaCorriente is null")
    public Iterable<Venta> mostrarVentasCuentasCorrientesImpagas();

    @Query("SELECT DATE_TRUNC('day', v.fechaYHora) AS fechaYHora, v.username, SUM(CASE WHEN v.pagoEfectivo IS NOT NULL THEN v.pagoEfectivo ELSE 0 END) AS sum_pagoEfectivo, SUM(CASE WHEN v.pagoDigital IS NOT NULL THEN v.pagoDigital ELSE 0 END) AS sum_pagoDigital, SUM(CASE WHEN v.pagoCuentaCorriente IS NOT NULL THEN v.pagoCuentaCorriente ELSE 0 END) AS sum_pagoCuentaCorriente FROM Venta v WHERE v.username IS NOT NULL AND v.username NOT IN ('SaldoDeudor', 'SaldoAFavor') AND v.fechaYHora >= :today GROUP BY DATE_TRUNC('day', v.fechaYHora), v.username ORDER BY MAX(v.id) DESC")
    //@Query("select v.fechaYHora, v.username, coalesce(sum(v.pagoEfectivo),'0'), coalesce(sum(v.pagoDigital),'0'), coalesce(sum(v.pagoCuentaCorriente),'0') from Venta v where v.username is not null and v.username !='SaldoDeudor' and v.username !='SaldoAFavor' and v.fechaYHora >=:today group by DAYOFMONTH(v.fechaYHora),v.username order by v.id desc")
    List<Object> obtenerVentasPorCaja(@Param("today") Date today);

    @Query("from Venta v where v.fechaYHora >=:today")
    //@Query("FROM Venta v WHERE v.fechaYHora BETWEEN CURRENT_DATE AND :today")
    public Iterable<Venta> mostrarVentas48hs(@Param("today") Date today);

    //@Query("SELECT DATE_TRUNC('day', v.fechaYHora), SUM(v.pagoEfectivo), v.username FROM Venta v WHERE v.fechaYHora >= :today GROUP BY DATE_TRUNC('day', v.fechaYHora), v.username")
    //@Query("select v.fechaYHora, sum(v.pagoEfectivo), v.username from Venta v where v.fechaYHora >= :today group by day(v.fechaYHora), month(v.fechaYHora), v.username")
    @Query("SELECT DATE_TRUNC('day', v.fechaYHora), SUM(v.pagoEfectivo), v.username FROM Venta v WHERE v.fechaYHora >= :fechaHoy and v.fechaYHora < :fechaManiana GROUP BY DATE_TRUNC('day', v.fechaYHora), v.username")
    public Iterable<Venta> mostrarVentasEstadisticas48hs(@Param("fechaHoy") Date fechaHoy, @Param("fechaManiana") Date fechaManiana);

    @Query("from Venta v where v.pagoDigital is not null and v.fechaYHora >= :fechaHoy and v.fechaYHora < :fechaManiana")
    List<Venta> mostrarMontosDigitales48hs(@Param("fechaHoy") Date fechaHoy, @Param("fechaManiana") Date fechaManiana);

    @Query("select sum(v.pagoEfectivo) from Venta v where v.fechaYHora >= :fechaHoy and v.fechaYHora < :fechaManiana")
    Float mostrarTotalEfectivoHoy(@Param("fechaHoy") Date fechaHoy, @Param("fechaManiana") Date fechaManiana);

    @Query("select sum(v.pagoDigital) from Venta v where v.fechaYHora >= :fechaHoy and v.fechaYHora < :fechaManiana")
    Float mostrarTotalDigitalHoy(@Param("fechaHoy") Date fechaHoy, @Param("fechaManiana") Date fechaManiana);

    @Query("select sum(v.pagoEfectivo) from Venta v where v.fechaYHora >=:fechaHoy and v.fechaYHora <:fechaManiana and v.username =:username")
    Float obtenerTotalVentaHoyPorUsuario(@Param("fechaHoy")Date fechaHoy, @Param("fechaManiana")Date fechaManiana, @Param("username")String username);

    @Query("from Venta v where v.clienteCuentaCorriente is not null and v.clienteCuentaCorriente =:cliente and v.pagoCuentaCorriente is not null and v.fechaCancelacionCuentaCorriente is null")
    Iterable<Venta> mostrarVentasCuentasCorrientesImpagasPorCliente(@Param("cliente") Integer cliente);

}