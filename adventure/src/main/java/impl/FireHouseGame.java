/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package impl;

import engine.GameDescription;
import parser.ParserOutput;
import type.AdvObject;
import type.AdvObjectContainer;
import type.Command;
import type.CommandType;
import type.Room;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import engine.GameObservable;
import engine.GameObserver;

/**
 * ATTENZIONE: La descrizione del gioco è fatta in modo che qualsiasi gioco
 * debba estendere la classe GameDescription. L'Engine è fatto in modo che possa
 * eseguire qualsiasi gioco che estende GameDescription, in questo modo si
 * possono creare più gioci utilizzando lo stesso Engine.
 *
 * Diverse migliorie possono essere applicate: - la descrizione del gioco
 * potrebbe essere caricate da file o da DBMS in modo da non modificare il
 * codice sorgente - l'utilizzo di file e DBMS non è semplice poiché all'interno
 * del file o del DBMS dovrebbe anche essere codificata la logica del gioco
 * (nextMove) oltre alla descrizione di stanze, oggetti, ecc...
 *
 * @author pierpaolo
 */
public class FireHouseGame extends GameDescription implements GameObservable {

    private final List<GameObserver> observer = new ArrayList<>();

    private ParserOutput parserOutput;

    private final List<String> messages = new ArrayList<>();

    /**
     *
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        messages.clear();
        //Commands
        Command nord = new Command(CommandType.NORD, "nord");
        nord.setAlias(new String[]{"n", "N", "Nord", "NORD"});
        getCommands().add(nord);
        Command iventory = new Command(CommandType.INVENTORY, "inventario");
        iventory.setAlias(new String[]{"inv"});
        getCommands().add(iventory);
        Command sud = new Command(CommandType.SOUTH, "sud");
        sud.setAlias(new String[]{"s", "S", "Sud", "SUD"});
        getCommands().add(sud);
        Command est = new Command(CommandType.EAST, "est");
        est.setAlias(new String[]{"e", "E", "Est", "EST"});
        getCommands().add(est);
        Command ovest = new Command(CommandType.WEST, "ovest");
        ovest.setAlias(new String[]{"o", "O", "Ovest", "OVEST"});
        getCommands().add(ovest);
        Command end = new Command(CommandType.END, "end");
        end.setAlias(new String[]{"end", "fine", "esci", "muori", "ammazzati", "ucciditi", "suicidati", "exit", "basta"});
        getCommands().add(end);
        Command look = new Command(CommandType.LOOK_AT, "osserva");
        look.setAlias(new String[]{"guarda", "vedi", "trova", "cerca", "descrivi"});
        getCommands().add(look);
        Command pickup = new Command(CommandType.PICK_UP, "raccogli");
        pickup.setAlias(new String[]{"prendi"});
        getCommands().add(pickup);
        Command open = new Command(CommandType.OPEN, "apri");
        open.setAlias(new String[]{});
        getCommands().add(open);
        Command push = new Command(CommandType.PUSH, "premi");
        push.setAlias(new String[]{"spingi", "attiva"});
        getCommands().add(push);
        Command use = new Command(CommandType.USE, "usa");
        use.setAlias(new String[]{"utilizza", "combina"});
        getCommands().add(use);
        //Rooms
        //Rooms
        Room entrance = new Room(0, "Ingresso laterale",
            "Sei davanti all'ingresso laterale della banca. La porta è protetta da un sistema di allarme.");

        entrance.setLook("La porta ha un pannello elettronico. Devi disattivare l'allarme per entrare.");

        Room security = new Room(1, "Sala sorveglianza",
            "Una stanza piena di monitor e telecamere che controllano tutta la banca.");

        security.setLook("Potresti usare un computer per manipolare il sistema di sicurezza.");

        Room corridor = new Room(2, "Corridoio di sicurezza",
            "Un lungo corridoio con sensori di movimento e laser di sicurezza.");

        corridor.setLook("I laser sembrano molto sensibili.");

        Room vault = new Room(3, "Caveau",
            "Il caveau principale della banca. Qui è custodito il denaro.");

        vault.setLook("La serratura del caveau è complessa.");

        Room escape = new Room(4, "Uscita di emergenza",
            "Questa è la via di fuga. Se hai il bottino puoi scappare.");
        
        //map
        entrance.setEast(security);

        security.setWest(entrance);
        security.setEast(corridor);

        corridor.setWest(security);
        corridor.setEast(vault);

        vault.setWest(corridor);
        vault.setEast(escape);

        escape.setWest(vault);
        getRooms().add(entrance);
        getRooms().add(security);
        getRooms().add(corridor);
        getRooms().add(vault);
        getRooms().add(escape);
        
        //objects

        // Tessera elettronica per entrare nella banca
        AdvObject keycard = new AdvObject(1, "tessera", 
            "Una tessera magnetica per aprire porte di sicurezza.");
        keycard.setAlias(new String[]{"badge", "card"});
        keycard.setPushable(false);
        keycard.setPush(false);
        entrance.getObjects().add(keycard);


        // Laptop per hackerare il sistema di sicurezza
        AdvObject laptop = new AdvObject(2, "laptop", 
            "Un laptop con software di hacking installato.");
        laptop.setAlias(new String[]{"computer", "pc"});
        laptop.setPushable(false);
        laptop.setPush(false);
        security.getObjects().add(laptop);


        // Specchio per deviare i laser nel corridoio
        AdvObject mirror = new AdvObject(3, "specchio", 
            "Uno specchio portatile. Potrebbe riflettere i raggi laser.");
        mirror.setAlias(new String[]{"vetro"});
        mirror.setPushable(false);
        mirror.setPush(false);
        corridor.getObjects().add(mirror);


        // Trapano elettrico per aprire il caveau
        AdvObject drill = new AdvObject(4, "trapano", 
            "Un potente trapano industriale per forzare serrature.");
        drill.setAlias(new String[]{"trapano_elettrico"});
        drill.setPushable(false);
        drill.setPush(false);
        security.getObjects().add(drill);


        // Contenitore: cassetta di sicurezza
        AdvObjectContainer safeBox = new AdvObjectContainer(5, "cassetta", 
            "Una piccola cassetta di sicurezza incassata nel muro.");
        safeBox.setAlias(new String[]{"cassaforte", "cassetta_sicurezza"});
        safeBox.setOpenable(true);
        safeBox.setPickupable(false);
        safeBox.setOpen(false);
        corridor.getObjects().add(safeBox);


        // Bottino dentro la cassetta
        AdvObject money = new AdvObject(6, "bottino", 
            "Una borsa piena di contanti.");
        money.setAlias(new String[]{"soldi", "denaro"});
        money.setPushable(false);
        money.setPush(false);
        safeBox.add(money);
         
        //Observer
        GameObserver moveObserver = new MoveObserver();
        this.attach(moveObserver);
        GameObserver invObserver = new InventoryObserver();
        this.attach(invObserver);
        GameObserver pushObserver = new PushObserver();
        this.attach(pushObserver);
        GameObserver lookatObserver = new LookAtObserver();
        this.attach(lookatObserver);
        GameObserver pickupObserver = new PickUpObserver();
        this.attach(pickupObserver);
        GameObserver openObserver = new OpenObserver();
        this.attach(openObserver);
        GameObserver useObserver = new UseObserver();
        this.attach(useObserver);
        
        //set starting room
        setCurrentRoom(entrance);
    }

    /**
     *
     * @param p
     * @param out
     */
    @Override
    public void nextMove(ParserOutput p, PrintStream out) {
        parserOutput = p;
        messages.clear();
        if (p.getCommand() == null) {
            out.println("Non ho capito cosa devo fare! Prova con un altro comando.");
        } else {
            Room cr = getCurrentRoom();
            notifyObservers();
            boolean move = !cr.equals(getCurrentRoom()) && getCurrentRoom() != null;
            if (!messages.isEmpty()) {
                for (String m : messages) {
                    if (m.length() > 0) {
                        out.println(m);
                    }
                }
            }
            if (move) {
                out.println(getCurrentRoom().getName());
                out.println("================================================");
                out.println(getCurrentRoom().getDescription());
            }
        }
    }

    /**
     *
     * @param o
     */
    @Override
    public void attach(GameObserver o) {
        if (!observer.contains(o)) {
            observer.add(o);
        }
    }

    /**
     *
     * @param o
     */
    @Override
    public void detach(GameObserver o) {
        observer.remove(o);
    }

    /**
     *
     */
    @Override
    public void notifyObservers() {
        for (GameObserver o : observer) {
            messages.add(o.update(this, parserOutput));
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getWelcomeMsg() {
        return "Sei appena tornato a casa e non sai cosa fare.\nTi ricordi che non hai ancora utilizzato quel fantastico regalo di tua zia Lina.\n"
                + "Sarà il caso di cercarlo e di giocarci!";
    }
}
