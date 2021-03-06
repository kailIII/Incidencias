package com.dexafree.incidencias;

//import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
//import android.view.Menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

//import android.view.MenuItem;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ImageView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import com.dexafree.incidencias.CardStack;
import com.dexafree.incidencias.CardUI;
import com.dexafree.incidencias.MyImageCard;

/**
 * Created by Carlos on 25/05/13.
 */
public class MainFavoritos extends SherlockActivity {


    public final static String XCOORDFAV = "com.dexafree.incidencias.XCOORDFAV";
    public final static String YCOORDFAV = "com.dexafree.incidencias.YCOORDFAV";

    ProgressDialog ShowProgress2;
    private CardUI mCardView2;
    public static ArrayList<Incidencia> IncidenciaList2 = MainActivity.IncidenciaList;
    public ArrayList<Favoritos> favList = Favoritos.FavoritosList;

    public static ArrayList<Incidencia> InciFavList;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cards);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        // init CardView
        mCardView2 = (CardUI) findViewById(R.id.cardsview);
        mCardView2.setSwipeable(true);

        firstTime2();


        Favoritos.FavoritosList.clear();

        try{
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("Favoritos.xml")));

            //ins = openFileInput("Favoritos.xml");

            int lines = load2();
            for(int i=0; i<lines; i++){
                String texto = fin.readLine();
//                Log.d("", "XML: " + texto);
                AndroidParseXMLActivity3 axa = new AndroidParseXMLActivity3();
//                Log.d("","Vamos a parsear");
                axa.parseXML(texto);
            }
            fin.close();
        } catch (Exception ex) {
//            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
//            Log.e("Ficheros", "Creando uno");

            try{
                OutputStreamWriter fcrear =
                        new OutputStreamWriter(
                                openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));
                fcrear.close();
//                Log.e("", "Creado XML");
            } catch (Exception exc) {
//                Log.e("","Ni siquiera se puede crear");
            }
        }

        if (Favoritos.FavoritosList.size() > 0){
            //TAREA DE CARGA DE XML Y PARSEO
            ShowProgress2 = ProgressDialog.show(MainFavoritos.this, "", "Cargando. Espere por favor...", true);
            ShowProgress2.setCancelable(true);
            new loadingTask2().execute("http://www.dexa-dev.es/incidencias/InciDGT.xml", "http://www.dexa-dev.es/incidencias/InciVascP.xml");
        } else {

            //final String PREFS_NAME = "MyPrefsFile2";

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainFavoritos.this);

            mCardView2.clearCards();

            mCardView2.addCard(new MyCard("No tienes ningun favorito añadido", "Entra al menú de Administrar y añade las carreteras y provincias que te interesen","Luego pulsa Actualizar", "Asegúrate de seleccionar también cada cuanto quieres que caduquen tus favoritos!"));
            mCardView2.refresh();

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

//        Log.d("DEX", "SE HA EJECUTADO");
    }



    //MENU ACTIONBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getSupportMenuInflater().inflate(R.menu.favs, menu);
        return true;
    }

    //GESTIONAR ITEMS ACTIONBAR
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.manage_favs:
                new Evento("Administrar Favoritos");
                startActivity(new Intent(this, ManageFavoritos.class));
                return true;
            case R.id.actualizarF:
                actualizar2();
                return true;

            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void firstTime2() {
        //final String PREFS_NAME = "MyPrefsFile2";

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainFavoritos.this);

        if (settings.getBoolean("my_first_time2", true)) {
            //the app is being launched for first time, do something
//            Log.d("Comments", "First time");

            mCardView2.addCard(new MyCard("No tienes ningun favorito añadido", "Entra al menú de Administrar y añade las carreteras y provincias que te interesen","Luego pulsa Actualizar", "Asegúrate de seleccionar también cada cuanto quieres que caduquen tus favoritos!"));
            mCardView2.addCard(new MyCard("Notificaciones", "En el menú de ajustes de la pantalla anterior podrás configurar las comprobaciones automáticas del estado del tráfico de tus lugares favoritos", "Si la aplicación detecta que hay una incidencia en algún lugar que has marcado como favorito, recibirás una notificación si lo deseas"));
            mCardView2.refresh();



            // first time task

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time2", false).commit();
        }

    }


    //CLASE LOADINGTASK
    class loadingTask2 extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            new Evento("Actualizado Favoritos");

            SAXHelper3 sh = null;
            int i = urls.length;

            for (int j = 0;j<i; j++){
                try {
                    sh = new SAXHelper3(urls[j]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                sh.parseContent("");
                //return "";

            }
            return "";
        }

        protected void onPostExecute(String s) {
//            Log.d("SIZE", IncidenciaList2.size() + "");
            if(IncidenciaList2.size() == 0){
                mCardView2.addCard(new MyCard("No hay incidencias a mostrar!", "Circule con cuidado","No olvide abrocharse el cinturón", "No utilice la aplicación mientras conduce", "Pare a descansar cada 2 horas de conducción"));
            }
            ShowProgress2.dismiss();
            mCardView2.refresh();
            Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_LONG).show();

        }
    }

    public void actualizar2() {
        //ELIMINAMOS LAS INCIDENCIAS EXISTENTES
        IncidenciaList2.clear();
        Favoritos.FavoritosList.clear();
        mCardView2.clearCards();


        try {
            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    openFileInput("Favoritos.xml")));

            //ins = openFileInput("Favoritos.xml");

            int lines = load2();
            for(int i=0; i<lines; i++){
                String texto = fin.readLine();
//                Log.d("", "XML: " + texto);
                AndroidParseXMLActivity3 axa = new AndroidParseXMLActivity3();
//                Log.d("","Vamos a parsear");
                axa.parseXML(texto);
            }
            fin.close();
        } catch (Exception ex) {
//            Log.e("Ficheros", "Error al leer fichero desde memoria interna");
            Log.e("Ficheros", "Creando uno");

            try{
                OutputStreamWriter fcrear =
                        new OutputStreamWriter(
                                openFileOutput("Favoritos.xml", Context.MODE_PRIVATE));
                fcrear.close();
//                Log.e("", "Creado XML");
            } catch (Exception exc) {
//                Log.e("","Ni siquiera se puede crear");
            }
        }

        //CARGAMOS NUEVAS INCIDENCIAS
        ShowProgress2 = ProgressDialog.show(MainFavoritos.this, "",
                "Cargando. Espere por favor...", true);
        new loadingTask2().execute("http://www.dexa-dev.es/incidencias/InciDGT.xml", "http://www.dexa-dev.es/incidencias/InciVascP.xml");

        //REFRESCAR LA VISTA DE LAS CARDS
        mCardView2.refresh();
    }

    public int load2() throws IOException {

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("Favoritos.xml")));
            String line;
            int lineCount = 0;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
                lineCount++;
            }
//            Log.d("", "Lines: " + lineCount);

            return lineCount;

        }
        catch (IOException e) {
//            Log.d("", "Ha saltado la excepcion de load");
            return 0;
        }

    }



    class SAXHelper3 {
        public HashMap<String, String> userList = new HashMap<String, String>();
        private URL url2;

        public SAXHelper3(String url1) throws MalformedURLException {
            this.url2 = new URL(url1);
        }

        public RSSHandler3 parseContent(String parseContent) {
            RSSHandler3 df = new RSSHandler3();
            try {

                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                xr.setContentHandler(df);
                xr.parse(new InputSource(url2.openStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return df;
        }
    }

    public void actualizar() {
        //ELIMINAMOS LAS INCIDENCIAS EXISTENTES
        mCardView2.clearCards();

        //CARGAMOS NUEVAS INCIDENCIAS
        ShowProgress2 = ProgressDialog.show(MainFavoritos.this, "",
                "Cargando. Espere por favor...", true);
        new loadingTask2().execute("http://www.dexa-dev.es/incidencias/InciDGT.xml", "http://www.dexa-dev.es/incidencias/InciVascP.xml");

        //REFRESCAR LA VISTA DE LAS CARDS
        mCardView2.refresh();

    }






    public boolean comparaPKs(Favoritos favActual, Incidencia incidenciaActual){

        float inicialIncidencia = Float.parseFloat(incidenciaActual.getPkInicio());
        float finalIncidencia = Float.parseFloat(incidenciaActual.getPkFin());

        int inicialFavorito = favActual.getPkInicial();
        int finalFavorito = favActual.getPkFinal();


        //NORMAL
        if (inicialIncidencia <= finalIncidencia){

            if (inicialFavorito <= finalFavorito){

                if (inicialIncidencia <= inicialFavorito && inicialIncidencia <= finalFavorito && finalIncidencia <= inicialFavorito && finalIncidencia <= finalFavorito){
                    return false;
                }
                else if (inicialIncidencia <= inicialFavorito && inicialIncidencia <= finalFavorito && finalIncidencia >= inicialFavorito && finalIncidencia <= finalFavorito){
                    return true;
                }
                else if (inicialIncidencia <= inicialFavorito && inicialIncidencia <= finalFavorito && finalIncidencia >= inicialFavorito && finalIncidencia >= finalFavorito){
                    return true;
                }
                else if (inicialIncidencia >= inicialFavorito && inicialIncidencia <= finalFavorito && finalIncidencia >= inicialFavorito && finalIncidencia <= finalFavorito){
                    return true;
                }
                else if (inicialIncidencia >= inicialFavorito && inicialIncidencia <= finalFavorito && finalIncidencia >= inicialFavorito && finalIncidencia >= finalFavorito){
                    return true;
                }
                else if (inicialIncidencia >= finalFavorito && inicialIncidencia >= inicialFavorito && finalIncidencia >= inicialFavorito && finalIncidencia >= finalFavorito){
                    return false;
                }
            }

            else if (inicialFavorito >= finalFavorito){

                if (inicialIncidencia <= finalFavorito && inicialIncidencia <= inicialFavorito && finalIncidencia <= finalFavorito && finalIncidencia <= inicialFavorito){
                    return false;
                }
                else if (inicialIncidencia <= finalFavorito && inicialIncidencia <= inicialFavorito && finalIncidencia >= finalFavorito && finalIncidencia <= inicialFavorito){
                    return true;
                }
                else if (inicialIncidencia <= finalFavorito && inicialIncidencia <= inicialFavorito && finalIncidencia >= finalFavorito && finalIncidencia >= inicialFavorito){
                    return true;
                }
                else if (inicialIncidencia >= finalFavorito && inicialIncidencia <= inicialFavorito && finalIncidencia >= finalFavorito && finalIncidencia <= inicialFavorito){
                    return true;
                }
                else if (inicialIncidencia >= finalFavorito && inicialIncidencia <= inicialFavorito && finalIncidencia >= finalFavorito && finalIncidencia >= inicialFavorito){
                    return true;
                }
                else if (inicialIncidencia >= finalFavorito && inicialIncidencia >= inicialFavorito && finalIncidencia >= finalFavorito && finalIncidencia >= inicialFavorito){
                    return false;
                }
            }
        }

        else if (inicialIncidencia >= finalIncidencia){

            if (inicialFavorito <= finalFavorito){

                if(finalIncidencia <= inicialFavorito && finalIncidencia <= finalFavorito && inicialIncidencia <= inicialFavorito && inicialIncidencia <= finalFavorito){
                    return false;
                }
                else if (finalIncidencia <= inicialFavorito && finalIncidencia <= finalFavorito && inicialIncidencia >= inicialFavorito && inicialIncidencia <= finalFavorito){
                    return true;
                }
                else if (finalIncidencia <= inicialFavorito && finalIncidencia <= finalFavorito && inicialIncidencia >= inicialFavorito && inicialIncidencia >= finalFavorito){
                    return true;
                }
                else if (finalIncidencia >= inicialFavorito && finalIncidencia <= finalFavorito && inicialIncidencia >= inicialFavorito && inicialIncidencia <= finalFavorito){
                    return true;
                }
                else if (finalIncidencia >= inicialFavorito && finalIncidencia <= finalFavorito && inicialIncidencia >= inicialFavorito && inicialIncidencia <= finalFavorito){
                    return true;
                }
                else if (finalIncidencia >= inicialFavorito && finalIncidencia >= finalFavorito && inicialIncidencia >= inicialFavorito && inicialIncidencia >= finalFavorito){
                    return false;
                }
            }

            else if (inicialFavorito >= finalFavorito){

                if (finalIncidencia <= finalFavorito && finalIncidencia <= inicialFavorito && inicialIncidencia < finalFavorito && inicialIncidencia < inicialFavorito){
                    return false;
                }
                else if (finalIncidencia <= finalFavorito && finalIncidencia <= inicialFavorito && inicialIncidencia >= finalFavorito && inicialIncidencia <= inicialFavorito){
                    return true;
                }
                else if (finalIncidencia <= finalFavorito && finalIncidencia <= inicialFavorito && inicialIncidencia >= finalFavorito && inicialIncidencia >= inicialFavorito){
                    return true;
                }
                else if (finalIncidencia >= finalFavorito && finalIncidencia <= inicialFavorito && inicialIncidencia >= finalFavorito && inicialIncidencia <= inicialFavorito){
                    return true;
                }
                else if (finalIncidencia >= finalFavorito && finalIncidencia <= inicialFavorito && inicialIncidencia >= finalFavorito && inicialIncidencia >= inicialFavorito){
                    return true;
                }
                else if (finalIncidencia >= finalFavorito && finalIncidencia >= inicialFavorito && inicialIncidencia >= finalFavorito && inicialIncidencia >= inicialFavorito){
                    return false;
                }
            }
        }
        return false;
    }

    public boolean comparador(String fechahora){


        String checker = fechahora.substring(11,12);

        if (checker.equals("-")){
            return false;
        }

        Calendar c = Calendar.getInstance();

        //Obtener datos del argumento fechahora
        String yearpas = fechahora.substring(0,4); //A?o
        String monthpas = fechahora.substring(5,7); //Mes
        String daypas = fechahora.substring(8,10); //Dia
        String fhs = fechahora.trim(); //Variable temporal para eliminar espacios al inicio y al final
        String horapas = fhs.substring(11,13); //Hora
        String minutospas = fhs.substring(14,16); //Minutos

        int horaInt = Integer.parseInt(horapas);
        int minutosInt = Integer.parseInt(minutospas);

        int yearpasInt = Integer.parseInt(yearpas);
        int monthpasInt = Integer.parseInt(monthpas)-1;
        int daypasInt = Integer.parseInt(daypas);

        SharedPreferences sphora = PreferenceManager.getDefaultSharedPreferences(this);
        String interv = sphora.getString("caduc_fav", "2");
        int intervInt = Integer.parseInt(interv);

        Date actual = c.getTime();
        c.add(Calendar.HOUR_OF_DAY, 0-intervInt);
        Date inicial = c.getTime();

        c.set(yearpasInt,monthpasInt,daypasInt,horaInt,minutosInt);

        Date dateInc = c.getTime();


//        Log.d("ACTUAL", actual.toString());
//        Log.d("INICIAL", inicial.toString());
//        Log.d("INCIDENCIA", dateInc.toString());

        if(actual.after(dateInc) && inicial.before(dateInc)){
            return true;
        } else {
            return false;
        }

    }





    public String getHora(String fechaHora) {

        //Log.i("", "FechaHora: " + fechaHora);
        String fhs = fechaHora.trim();
        //Log.i("", "fhs: " + fhs);
        String hora = fhs.substring(11,13);
        //Log.i("", "hora: " + hora);
        String minutos = fhs.substring(14,16);
        //Log.i("", "minutos: " + minutos);
        return hora + ":" + minutos + "  ";

    }

    public int incIcono(String tipo, String nivel){

        if (tipo.equalsIgnoreCase("METEOROLOGICA")){
            if (nivel.equals("VERDE")){
                return R.drawable.meteo_verde;
            }
            if (nivel.equalsIgnoreCase("ROJO")){
                return R.drawable.meteo_rojo;
            }
            if (nivel.equalsIgnoreCase("AMARILLO")){
                return R.drawable.meteo_amarillo;
            }
            if (nivel.equalsIgnoreCase("NEGRO")){
                return R.drawable.meteo_negro;
            }
        }

        else if (tipo.equalsIgnoreCase("CONOS")){
            if (nivel.equals("VERDE")){
                return R.drawable.conos_verde;
            }
            if (nivel.equalsIgnoreCase("ROJO")){
                return R.drawable.conos_rojo;
            }
            if (nivel.equalsIgnoreCase("AMARILLO")){
                return R.drawable.conos_amarillo;
            }
            if (nivel.equalsIgnoreCase("NEGRO")){
                return R.drawable.conos_negro;
            }
        }

        else if (tipo.equalsIgnoreCase("RETENCION")){
            if (nivel.equals("VERDE")){
                return R.drawable.retencion_verde;
            }
            if (nivel.equalsIgnoreCase("ROJO")){
                return R.drawable.retencion_rojo;
            }
            if (nivel.equalsIgnoreCase("AMARILLO")){
                return R.drawable.retencion_amarillo;
            }
            if (nivel.equalsIgnoreCase("NEGRO")){
               return R.drawable.retencion_negro;
            }
        }

        else if (tipo.equalsIgnoreCase("CONOS")){
            if (nivel.equals("VERDE")){
                return R.drawable.conos_verde;
            }
            if (nivel.equalsIgnoreCase("ROJO")){
                return R.drawable.conos_rojo;
            }
            if (nivel.equalsIgnoreCase("AMARILLO")){
                return R.drawable.conos_amarillo;
            }
            if (nivel.equalsIgnoreCase("NEGRO")){
                return R.drawable.conos_negro;
            }
        }

        else if (tipo.equalsIgnoreCase("OBRAS")){
            if (nivel.equals("VERDE")){
                return R.drawable.obras_verde;
            }
            if (nivel.equalsIgnoreCase("ROJO")){

                return R.drawable.obras_rojo;
            }
            if (nivel.equalsIgnoreCase("AMARILLO")){

                return R.drawable.obras_amarillo;
            }
            if (nivel.equalsIgnoreCase("NEGRO")){

                return R.drawable.obras_negro;
            }
        }
        return R.drawable.conos_verde;
    }

    public boolean checkProvincia(Incidencia currentIncidencia) {

        for (int i = 0; i<favList.size(); i++){

            if ((currentIncidencia.getProvincia()).equalsIgnoreCase(favList.get(i).getProvincia())){
                return true;
            }
            else if ((currentIncidencia.getMatricula()).equalsIgnoreCase((favList.get(i).getProvincia()))){
                return true;
            }
        }

        return false;


    }

    public boolean checkTesteo() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(
                        MainFavoritos.this);

        if ( pref.getBoolean("testeo", false)) {
            return true;
        }
        else {
            return false;
        }


    }


    public void creaCard(Incidencia currentIncidencia){

        final double x = currentIncidencia.getX();
        final double y = currentIncidencia.getY();

        if(x!= 0.0){
            MyCardMap card = new MyCardMap(getHora(currentIncidencia.getFechahora()) + currentIncidencia.getCarretera() + "  -  " + currentIncidencia.getPoblacion(), "CAUSA: " + currentIncidencia.getCausa(), "KM INICIAL: " + currentIncidencia.getPkInicio() + "        KM FINAL: " + currentIncidencia.getPkFin(), "SENTIDO: " + currentIncidencia.getSentido(), "HACIA: " + currentIncidencia.getHacia(), currentIncidencia.getX(), currentIncidencia.getY());
            card.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if((GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainFavoritos.this)) == ConnectionResult.SUCCESS){
                        new Evento("Mapa Favoritos");
                        Context context = getApplicationContext();
                        Intent intent = new Intent(context, MapFavActivity.class);
                        intent.putExtra(XCOORDFAV,x);
                        intent.putExtra(YCOORDFAV,y);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainFavoritos.this, "Asegúrese de que su dispositivo tiene instalado y actualizado el servicio Google Play Services", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            mCardView2.addCard(card);
        }

        else{

            MyCard card = new MyCard(getHora(currentIncidencia.getFechahora()) + currentIncidencia.getCarretera() + "  -  " + currentIncidencia.getPoblacion(), "CAUSA: " + currentIncidencia.getCausa(), "KM INICIAL: " + currentIncidencia.getPkInicio() + "        KM FINAL: " + currentIncidencia.getPkFin(), "SENTIDO: " + currentIncidencia.getSentido(), "HACIA: " + currentIncidencia.getHacia(), currentIncidencia.getX(), currentIncidencia.getY());

            mCardView2.addCard(card);

        }




        MyImageCard imCard = new MyImageCard((currentIncidencia.getTipo()), incIcono(currentIncidencia.getTipo(), currentIncidencia.getNivel()), "KM INI: " + currentIncidencia.getPkInicio(),"KM FIN: " +  currentIncidencia.getPkFin(),"SENTIDO: " +  currentIncidencia.getSentido());

        if(x!= 0.0){
            imCard.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if((GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainFavoritos.this)) == ConnectionResult.SUCCESS){
                        new Evento("Mapa Favoritos");
                        Context context = getApplicationContext();
                        Intent intent = new Intent(context, MapFavActivity.class);
                        intent.putExtra(XCOORDFAV,x);
                        intent.putExtra(YCOORDFAV,y);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainFavoritos.this, "Asegúrese de que su dispositivo tiene instalado y actualizado el servicio Google Play Services", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

        mCardView2.addCardToLastStack(imCard);


    }



    class RSSHandler3 extends DefaultHandler {

        private Incidencia currentIncidencia = new Incidencia();
        StringBuffer chars = new StringBuffer();

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes atts) {

            chars = new StringBuffer();
            if (localName.equalsIgnoreCase("incidencia")) {

            }
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {

            if (localName.equalsIgnoreCase("tipo")
                    && currentIncidencia.getTipo() == null) {
                currentIncidencia.setTipo(chars.toString().trim());

            }
            if (localName.equalsIgnoreCase("autonomia")
                    && currentIncidencia.getAutonomia() == null) {
                currentIncidencia.setAutonomia(chars.toString().trim());

            }

            if (localName.equalsIgnoreCase("provincia")
                    && currentIncidencia.getProvincia() == null) {
                currentIncidencia.setProvincia(chars.toString().trim());
            }

            if (localName.equalsIgnoreCase("matricula")
                    && currentIncidencia.getMatricula() == null) {
                currentIncidencia.setMatricula(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("causa")
                    && currentIncidencia.getCausa() == null) {
                currentIncidencia.setCausa(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("poblacion")
                    && currentIncidencia.getPoblacion() == null) {
                currentIncidencia.setPoblacion(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("fechahora_ini")
                    && currentIncidencia.getFechahora() == null) {
                currentIncidencia.setFechahora(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("nivel")
                    && currentIncidencia.getNivel() == null) {
                currentIncidencia.setNivel(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("carretera")
                    && currentIncidencia.getCarretera() == null) {
                currentIncidencia.setCarretera(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("pk_inicial")
                    && currentIncidencia.getPkInicio() == null) {
                currentIncidencia.setPkInicio(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("pk_final")
                    && currentIncidencia.getPkFin() == null) {
                currentIncidencia.setPkFin(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("sentido")
                    && currentIncidencia.getSentido() == null) {
                currentIncidencia.setSentido(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("hacia")
                    && currentIncidencia.getHacia() == null) {
                currentIncidencia.setHacia(chars.toString().trim());
            }
            if (localName.equalsIgnoreCase("x")
                    && currentIncidencia.getX() == 0.0f) {

                String xTemp = chars.toString().trim();
                if (xTemp != null && !(xTemp.equalsIgnoreCase("null")) && !(xTemp.equalsIgnoreCase(""))){
                    double x = Double.parseDouble(xTemp);
                    currentIncidencia.setX(x);
                }
            }
            if (localName.equalsIgnoreCase("y")
                    && currentIncidencia.getY() == 0.0f) {
                String yTemp = chars.toString().trim();
                if (yTemp != null && !(yTemp.equalsIgnoreCase("null")) && !(yTemp.equalsIgnoreCase(""))){
                    double y = Double.parseDouble(yTemp);
                    currentIncidencia.setY(y);
                }
            }
            if(localName.equalsIgnoreCase("ref_incidencia") && currentIncidencia.getRefIncidencia() == null){
                currentIncidencia.setRefIncidencia(chars.toString());
            }

            if (localName.equalsIgnoreCase("incidencia")) {
                if (checkProvincia(currentIncidencia) == true) {
                    if (comparador(currentIncidencia.getFechahora())) {
                        for (int i=0; i<favList.size();i++){
                            if (currentIncidencia.getCarretera().equalsIgnoreCase(favList.get(i).getCarretera())){
                                IncidenciaList2.add(currentIncidencia);
                                creaCard(currentIncidencia);

                            }
                        }
                    }
                } else {
                    if(comparador(currentIncidencia.getFechahora())){
                        for(int i=0; i<favList.size(); i++){
                            if(currentIncidencia.getCarretera().equalsIgnoreCase(favList.get(i).getCarretera())){
                                if(comparaPKs(favList.get(i), currentIncidencia)){
                                    IncidenciaList2.add(currentIncidencia);
                                    creaCard(currentIncidencia);
                                }
                            }
                        }
                    }
                }
                currentIncidencia = new Incidencia();
            }
        }

        @Override
        public void characters(char ch[], int start, int length) {
            chars.append(new String(ch, start, length));
        }
    }

    public class AndroidParseXMLActivity3 {

        private void parseXML(String contenido) {
            try {

                Log.w("AndroidParseXMLActivity", "Start");
                /** Handling XML */
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();

                ItemXMLHandler myXMLHandler = new ItemXMLHandler();
                xr.setContentHandler(myXMLHandler);
                InputSource inStream = new InputSource();
                Log.w("AndroidParseXMLActivity", "Parse1");


                inStream.setCharacterStream(new StringReader(contenido.toString()));
                Log.w("AndroidParseXMLActivity", "Parse2");

                xr.parse(inStream);
                Log.w("AndroidParseXMLActivity", "Parse3");


                Log.w("AndroidParseXMLActivity", "Done");


                int i = favList.size();
                for (int j = 0; j<i ; j++){
                    String prov = favList.get(j).getProvincia().toString();
//                    Log.d("PROV", prov);
                    SharedPreferences settings = getSharedPreferences("MyPref", 0);
                    settings.edit().putBoolean(prov, true).commit();
                }

            }
            catch (Exception e) {
                Log.w("AndroidParseXMLActivity",e );
            }


        }

    }

}
