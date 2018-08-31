package com.andreitop.xml.config;

import com.andreitop.xml.mount.Mount;
import com.andreitop.xml.mount.Tiger;
import com.andreitop.xml.mount.Wolf;
import com.andreitop.xml.unit.Human;
import com.andreitop.xml.unit.Orc;
import com.andreitop.xml.unit.Troll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.xml.bind.annotation.XmlType;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@ComponentScan("com.andreitop.xml")
@PropertySource("classpath:config/heroes.properties")
public class AppConfig {

    @Value("${character.created}")
    private String CHARACTER_CREATION_DATE;

    @Bean
    public Wolf frostWolf(){
        return new Wolf();
    }

    @Bean
    public Tiger shadowTiger(){
        return new Tiger();
    }

    @Bean
    public Human knight(){
        String soulBlade = "soulBlade";
        String thunderFury = "thunderFury";
        Mount shadowTiger = shadowTiger();
        return new Human(shadowTiger, thunderFury,soulBlade);
    }

    @Bean
    public Orc trall(){
        Mount frostWolf = frostWolf();
        Orc orc = new Orc(frostWolf);
        orc.setColorCode(9);
        orc.setWeapon("furryAxe");
        return orc;
    }

    @Bean
    public SimpleDateFormat dateFormatter(){
        return new SimpleDateFormat("dd/mm/yyyy");
    }

    @Bean
    public Map<String, Mount> trollMountMap(){
        Map<String, Mount> trollMountMap = new HashMap<>();
        trollMountMap.put("m1", frostWolf());
        trollMountMap.put("m2", shadowTiger());
        return trollMountMap;
    }

    @Bean
    public Set<Mount> trollMountSet(){
        Set<Mount> trollMountSet = new LinkedHashSet<>();
        trollMountSet.add(shadowTiger());
        trollMountSet.add(frostWolf());
        return trollMountSet;
    }

    @Bean
    public Troll zulJin(){
        Troll zulJin = new Troll();
        zulJin.setColorCode(ThreadLocalRandom.current().nextInt(1,10));

        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            String filename = "config/heroes.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(filename);
            properties.load(inputStream);
            String creationDate = properties.getProperty("character.created");
            zulJin.setCreationDate(dateFormatter().parse(creationDate));
        } catch (Exception ex) {

        } finally {
            if (inputStream != null) {
                try{
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
//      zulJin.setCreationDate(dateFormatter().parse(CHARACTER_CREATION_DATE)); Or just like this
        List<Mount> mountList = new ArrayList<>();
        mountList.add(com.andreitop.xml.unit.Troll.DEFAULT_MOUNT);
        mountList.add(null);
        mountList.add(shadowTiger());
        zulJin.setListOfMounts(mountList);
        zulJin.setSetOfMounts(trollMountSet());
        zulJin.setMapOfMounts(trollMountMap());
        return zulJin;
    }

}
