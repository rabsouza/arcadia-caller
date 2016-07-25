package br.com.battista.arcadia.caller.repository;

import static br.com.battista.arcadia.caller.constants.EntityConstant.INITIAL_INDEX_KEY;
import static br.com.battista.arcadia.caller.model.KeyCampaign.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.googlecode.objectify.Objectify;

import br.com.battista.arcadia.caller.model.KeyCampaign;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class KeyCampaignRepository {

    @Autowired
    private Objectify objectifyRepository;

    public String nextKey() {
        log.info("Find last key!");

        KeyCampaign key = objectifyRepository
                        .load()
                        .type(KeyCampaign.class)
                        .order("-index")
                        .first()
                        .now();

        Long index = INITIAL_INDEX_KEY;
        if (key != null) {
            index = key.getIndex() + 1;
        }
        KeyCampaign newKey = builder().index(index).key(PREFIX_KEY.concat(String.valueOf(index))).build();

        log.info("Save the new Key: {}!", newKey);
        objectifyRepository
                        .save()
                        .entity(newKey)
                        .now();
        return newKey.getKey();
    }
}
