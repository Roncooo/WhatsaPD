# WhatsaPD

## Accessibilità e inclusive design

L'applicazione è stata sviluppata per mostrare le linee guida di Android nell'ambito dell'inclusive design e dell'accessibilità. Il progetto è stato creato ponendo attenzione alle diverse necessità che gli utenti possono avere. 

L'utente può utilizzare i servizi e le impostazioni per l'accessibilità che preferisce (ad esempio TalkBack, Select to Speak, Switch Access, Voice Access etc.) e l'applicazione risponderà correttamente.

## Descrizione del progetto

L'applicazione è una semplice versione di una guida turistica per la città di Padova che permette di:

+ leggere alcune informazioni sui monumenti inseriti nel database
+ leggere delle recensioni su ogni monumento lasciate dagli altri utenti o scrivere le proprie
+ cercare i monumenti per nome o scegliendoli tra quelli preferiti
+ vedere quali sono i monumenti più vicini alla posizione corrente in linea d'aria

I dati relativi ai punti di interesse sono tratti da [Wikipedia](https://it.wikipedia.org/) e dai vari siti ufficiali.
Le recensioni lasciate dagli utenti sono fittizie e generate con sistemi di intelligenza artificiale.

## Come eseguire l'applicazione

Se si usa un computer, è sufficiente aprire il progetto in Android Studio, aspettare le varie fasi di build e infine eseguire con l'apposita funzione (su dispositivo fisico o emulatore). 
È fornito anche il file [WhatsaPD-v1.0-release.apk](app/release/WhatsaPD-v1.0-release.apk) che permette una facile installazione su dispositivo mobile senza bisogno di passare per Android Studio.

## Note sul progetto

+ L'app è stata sviluppata e testata utilizzando l'emulatore Pixel 2 API 34 e su dispositivi fisici con  
  Android da 9 a 13
+ Sono state utilizzate alcune librerie esterne tra cui
    + `Room` per il funzionamento del database
    + `opencsv` per la fase di prepopolamento del database
    + `Google Play Services Location` per l'accesso alla posizione (si noti che questo servizio
      non è disponibile per gli smartphone non dotati di servizi Google, come gli Huawei)
+ Il codice è stato analizzato con `LINT` che ha permesso di trovare e correggere errori e warning

## Dettagli sul codice

### Struttura e design pattern

L'applicazione consiste in un'`activity` [MainActivity](app/src/main/java/it/unipd/dei/esp/whatsapd/MainActivity.kt) e quattro `fragment` ([HomeFragment](app/src/main/java/it/unipd/dei/esp/whatsapd/ui/home/HomeFragment.kt), [FavouriteFragment](app/src/main/java/it/unipd/dei/esp/whatsapd/ui/favourites/FavouritesFragment.kt), [NearMeFragment](app/src/main/java/it/unipd/dei/esp/whatsapd/ui/nearme/NearMeFragment.kt), [PoiFragment](app/src/main/java/it/unipd/dei/esp/whatsapd/ui/poi/PoiFragment.kt)). 
Per gestire la navigazione tra i vari `fragment` è stato usato [Navigation](app/src/main/java/it/unipd/dei/esp/whatsapd/MainActivity.kt), che agisce attraverso [mobile_navigation.xml](app/src/main/res/navigation/mobile_navigation.xml). Per il passsaggio di dati tra i `fragment` è stato usato `Safe Args`.
I `fragment` costituiscono la view del pattern "model-view-adapter" utilizzato nel progetto. 
Il modello coincide con la classe [PoiReviewRepository.kt](app/src/main/java/it/unipd/dei/esp/whatsapd/repository/PoiReviewRepository.kt) che permette diverse fonti per i dati ma al momento è unicamente alimentato dal database `room`.
Ogni `fragment` non interagisce direttamente con la repository bensì con uno o più componenti `view-model` per una migliore divisione delle responsabilità. I dati ottenuti da questi `view-model` vengono passati alle relative classi `adapter` che si occupano di riempire gli elementi grafici (in particolare `RecyclerView`).

### Database

Il database `room` utilizzato consiste in due entità (rispettivamente [Poi.kt](app/src/main/java/it/unipd/dei/esp/whatsapd/repository/database/Poi.kt) e [Review.kt](app/src/main/java/it/unipd/dei/esp/whatsapd/repository/database/Review.kt)) su cui facciamo affidamento per il salvataggio persistente dei dati.
Al primo avvio dell'app avviene la fase di prepopolamento che grazie a [CSVParser.kt](app/src/main/java/it/unipd/dei/esp/whatsapd/repository/database/CSVParser.kt) trasforma i due file [pois.csv](app/src/main/res/raw/pois.csv) e [reviews.csv](app/src/main/res/raw/reviews.csv) in oggetti e li inserisce nel database.

### Salvataggio dello statoWebView

L'applicazione non utilizza particolari componenti dinamici bensì solamente `TextView`, `WebView`, `ImageView` e `RatingBar` che vengono riempite come detto al punto precedente con informazioni tratte dal database che come già detto rappresenta la fonte di riferimento per il salvataggio persistente dei dati.
Al momento di inserimento di una nuova review ci si interfaccia con un widget che usa due `EditText` e una `Ratingbar`. Prima di premere 'invio', la review non è inserita nel database e quindi non è salvata in modo persistente. Nonostante questo, Android salva di default il contenuto di questi elementi rendendolo nuovamente disponibile all'utente quando l'applicazione viene ripresa dopo essere andata in background. Se però l'app viene arrestata o uccisa, questi dati non vengono salvati. La stessa cosa vale per la `SearchBar` presente nella schermat 'home'.
Per il contesto dell'applicazione, abbiamo ritenuto soddisfacente questo comportamento di Android.


# Autori

- [Beatrice Bellesso](https://github.com/isgreta)
- [Greta Brocco](https://github.com/SleepyGutierrez)
- [Francesco Roncolato](https://github.com/Roncooo)
