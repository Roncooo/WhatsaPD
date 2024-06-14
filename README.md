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

I dati relativi ai punti di interesse sono tratti da [Wikipedia](https://it.wikipedia.org/) e adattati con [ChatGPT](https://chatgpt.com/)

### Note sul progetto

+ L'app è stata sviluppata e testata utilizzando l'emulatore Pixel 2 API 34 e su dispositivi fisici con  
  Android da 9 a 13
+ Sono state utilizzate alcune librerie esterne tra cui
    + `Room` per il funzionamento del database
    + `opencsv` per la fase di prepopolamento del database
    + `Google Play Services Location` per l'accesso alla posizione (si noti che questo servizio
      non è disponibile per gli smartphone non dotati di servizi Google, come gli Huawei)
+ Il codice è stato analizzato con `LINT` che ha permesso di trovare e correggere errori e warning

### Dettagli sul codice
L'applicazione consiste in un'`activity` [MainActivity](app/src/main/java/it/unipd/dei/esp/whatsapd/MainActivity.kt)
e quattro `fragment` ([HomeFragment](app/src/main/java/it/unipd/dei/esp/whatsapd/ui/home/HomeFragment.kt),
[FavouriteFragment](app/src/main/java/it/unipd/dei/esp/whatsapd/ui/favourites/FavouritesFragment.kt), 
[NearMeFragment](app/src/main/java/it/unipd/dei/esp/whatsapd/ui/nearme/NearMeFragment.kt), 
[PoiFragment](app/src/main/java/it/unipd/dei/esp/whatsapd/ui/poi/PoiFragment.kt)). 
Per gestire la navigazione tra i `fragment` è stato usato [Navigation](app/src/main/java/it/unipd/dei/esp/whatsapd/MainActivity.kt).
Ogni `fragment` usa un `adapter` per 


### Autori

- [Beatrice Bellesso](https://github.com/isgreta)
- [Greta Brocco](https://github.com/SleepyGutierrez)
- [Francesco Roncolato](https://github.com/Roncooo)
