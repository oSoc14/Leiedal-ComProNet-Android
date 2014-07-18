package osoc.leiedal.android.aandacht.database;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;

import java.util.Random;

import osoc.leiedal.android.aandacht.contentproviders.AandachtContentProvider;
import osoc.leiedal.android.aandacht.database.model.messages.Message;
import osoc.leiedal.android.aandacht.database.model.reports.Report;

/**
 * Created by Maarten on 11/07/2014.
 */
public class DummyData {

    public static int REPORT_COUNT = 10;
    public static int MESSAGE_COUNT = 25; // per report

    public static String[] STATUSES_LABELS = {ReportsTable.STATUS_ACTIVE, ReportsTable.STATUS_PENDING, ReportsTable.STATUS_DENIED, ReportsTable.STATUS_FINISHED};
    public static double[] STATUSES_CHANCES = {0.01, 0.02, 0.17, 0.80};

    private static final Random rnd = new Random();

    private static final double[] LOCATION_GHENT = {51.0537439, 3.7241694};
    private static final double[] LOCATION_KORTRIJK = {50.8249558, 3.2643610};

    private static double[] LOCATION = LOCATION_GHENT;

    private static final String[] ADDRESSES = {"Brielstraat 46 9860 Oosterzele", "Jules Destrelaan 67 9050 Gentbrugge", "Leenstraat 31 9890 Vurste", "Ebergiste De Deynestraat 1 9000 Gent", "Dennendreef 53 9850 Landegem", "Lelienlaan 1 9200 Dendermonde", "Broekstraat 27 9255 Buggenhout", "Groot Begijnhof 10 9040 Sint-Amandsberg", "Wijnveld 255 9112 Sinaai", "Leuzesesteenweg 138 9600 Ronse", "Leernsesteenweg 53 9800 Bachte-Maria-Leerne", "Blijdorpstraat 3 9255 Buggenhout", "Baleunisstraat 3 9200 Dendermonde", "Vekenstraat 1 9255 Buggenhout", "Botermelkstraat 201 9300 Aalst", "Asselkouter 34 9820 Munte", "Repingestraat 12 1570 Vollezele", "Langerbrugsestraat 258 9940 Evergem", "Onze-Lieve-Vrouwplein 31 9100 Sint-Niklaas", "Drongenplein 26 9031 Drongen", "Kasteeldreef 2 9630 Beerlegem", "Stationsstraat 3 9690 Kluisbergen", "Oude Zandstraat 92 9120 Beveren-Waas", "Pamelstraat Oost 368 9400 Ninove", "Poststraat 6 9160 Lokeren", "Industriepark 6 9820 Merelbeke", "Leliestraat 1 9620 Zottegem", "Kasterstraat 81 9230 Wetteren", "Beekstraat 27 9031 Drongen", "Beverse Dijk 63 9120 Kallo", "Abingdonstraat 101 9100 Sint-Niklaas", "Gentsesteenweg 54 9160 Lokeren", "Molendreef 16 9920 Lovendegem", "Haarkenstraat 32 A 9850 Landegem", "Waaistraat 6 9900 Eeklo", "Botestraat 131-133 9032 Wondelgem", "Kramershoek 39 9940 Evergem", "Bolwerkstraat 11 9940 Evergem", "Brusselsesteenweg 153 9090 Melle", "Wolfputstraat 106 9041 Oostakker", "Klaverveld 1 9255 Buggenhout", "Krekelstraat 17 9160 Lokeren", "Maisstraat 268 9000 Gent", "Riedekens Z/n 9700 Oudenaarde", "Vincent Evrardlaan 20 9050 Gentbrugge", "Kartuizersstraat 51 8310 Sint-Kruis", "Kazernestraat 35A 9100 Sint-Niklaas", "Nokere Pontweg 1 9772 Wannegem-Lede", "Eisdale 1 9600 Ronse", "Van Den Heckestraat 43 9050 Ledeberg", "Nachtegalenpark 1 9840 De Pinte", "Paardekerkhof 4 9990 Maldegem", "Nieuwgoedlaan 15-23 9800 Deinze", "Nijverheidsstraat 9 9950 Waarschoot", "Raymond De Hemptinnelaan 33 9030 Mariakerke", "Keizersvest 18 9000 Gent", "Jozef Guislainstraat 47-49 9000 Gent", "Gentsesteenweg 155 9230 Wetteren", "Schuitstraat 16 9401 Pollare", "Kwatrechtsteenweg 168 9230 Wetteren", "Edingseweg 543 9500 Viane", "Brusselsesteenweg 375 A 9090 Melle", "Poeldendries 32 9850 Landegem", "Sint-Lievenspoortstraat 220 9000 Gent", "Patershoek 4 9111 Belsele", "Daknamdorp 54 9160 Lokeren", "Karmelietenstraat 12-14 9500 Geraardsbergen", "Steenweg 2 9810 Eke", "Fraterstraat 36 9820 Merelbeke", "Zonnestraat 13 9810 Eke"};

    // ------------------------------

    public static void InjectDummyData(Activity a) {
        System.out.println("[DummyData] InjectDummyData(" + a + ")");

        //delete old data
        a.getContentResolver().delete(AandachtContentProvider.CONTENT_URI_REPORTS, null, null);
        a.getContentResolver().delete(AandachtContentProvider.CONTENT_URI_MESSAGES, null, null);

        ContentValues[] reports = new ContentValues[REPORT_COUNT];
        ContentValues[] messages = new ContentValues[MESSAGE_COUNT];
        for (int i = 0; i < REPORT_COUNT; i++) {
            reports[i] = Report().getContentValues();
            long time = System.currentTimeMillis();
            for(int j = 0; j < MESSAGE_COUNT; j++) {
                messages[j] = new Message(
                        i,
                        (i % 2 == 0 ? "Dispatch" : "Ik"),
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum vestibulum, urna sit amet tristique mollis, nunc quam rhoncus erat, eget semper sapien massa ac tellus. Nam pulvinar vitae augue et viverra",
                        time += rnd.nextInt(30)
                    ).getContentValues();
            }
        }
        a.getContentResolver().bulkInsert(AandachtContentProvider.CONTENT_URI_REPORTS, reports);
    }

    public static void injectReport(ContentResolver cr, Report r){
        System.out.println("[DummyData] InjectReport(" + cr + ")");

        cr.insert(AandachtContentProvider.CONTENT_URI_REPORTS, r.getContentValues());
    }

    // ------------------------------

    private static Report Report() {
        return new Report(
                Description(),
                Address(),
                Latitude(),
                Longitude(),
                Status(),
                TimeStart(),
                TimeEnd()
        );
    }

    private static String Description() {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum vestibulum," +
                "urna sit amet tristique mollis, nunc quam rhoncus erat, eget semper sapien massa" +
                "ac tellus. Nam pulvinar vitae augue et viverra. Ut faucibus diam molestie faucibus" +
                "scelerisque. Interdum et malesuada fames ac ante ipsum primis in faucibus. Quisque" +
                "semper volutpat velit nec fermentum. Cum sociis natoque penatibus et magnis dis" +
                "parturient montes, nascetur ridiculus mus. Sed laoreet magna nisi, eu ultrices est" +
                "hendrerit id. Nullam placerat vitae eros eget mollis.";
    }

    private static String Address() {
        return ADDRESSES[rnd.nextInt(ADDRESSES.length)];
    }

    private static double Latitude() {
        return LOCATION[0] + rnd.nextDouble() * 0.008 - 0.004;
    }

    private static double Longitude() {
        return LOCATION[1] + rnd.nextDouble() * 0.008 - 0.004;
    }

    private static String Status() {
        double r = rnd.nextDouble();
        double c = 0;
        for (int i = 0; i < STATUSES_LABELS.length; i++) {
            c += STATUSES_CHANCES[i];
            if (r <= c) {
                return STATUSES_LABELS[i];
            }
        }
        return STATUSES_LABELS[STATUSES_LABELS.length - 1];
    }

    private static long TimeStart() {
        long curr = System.currentTimeMillis();
        return (curr / 1000) - rnd.nextInt(3600);
    }

    private static long TimeEnd() {
        long curr = System.currentTimeMillis();
        return (curr / 1000) + rnd.nextInt(3600);
    }

}
