/*
    Programado por Luis Cabrera Benito
  ____          _____               _ _           _
 |  _ \        |  __ \             (_) |         | |
 | |_) |_   _  | |__) |_ _ _ __ _____| |__  _   _| |_ ___
 |  _ <| | | | |  ___/ _` | '__|_  / | '_ \| | | | __/ _ \
 | |_) | |_| | | |  | (_| | |   / /| | |_) | |_| | ||  __/
 |____/ \__, | |_|   \__,_|_|  /___|_|_.__/ \__, |\__\___|
         __/ |                               __/ |
        |___/                               |___/


    Blog:       https://parzibyte.me/blog
    Ayuda:      https://parzibyte.me/blog/contrataciones-ayuda/
    Contacto:   https://parzibyte.me/blog/contacto/
*/
package pl.codeleak.demos.sbt.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

class Utiles {
    static String obtenerFechaYHoraActualString() {
        String formato = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
        LocalDateTime ahora = LocalDateTime.now();
        return formateador.format(ahora);
    }

    static Date obtenerFechaYHoraActualDate() {
        /*TimeZone zonaArgentina = TimeZone.getTimeZone("America/Argentina/Buenos_Aires");
        java.util.Calendar calendario = java.util.Calendar.getInstance(zonaArgentina);
        return calendario.getTime();*/

        ZonedDateTime fechaHoraBuenosAires = ZonedDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        Date fechaYHora = Date.from(fechaHoraBuenosAires.toInstant());
        return fechaYHora;

    }
}
