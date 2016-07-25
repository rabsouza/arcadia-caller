package br.com.battista.arcadia.caller.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.appengine.repackaged.com.google.common.collect.Lists;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.enuns.GroupCardEnum;
import br.com.battista.arcadia.caller.model.enuns.TypeCardEnum;
import br.com.battista.arcadia.caller.repository.CardRepository;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {

    private final String name = "card01";
    private final String key = "key01";
    private final TypeCardEnum type = TypeCardEnum.UPGRADE;
    private final GroupCardEnum group = GroupCardEnum.NONE;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Test
    public void shouldGetAllCards() {
        Card card = Card.builder().name(name).key(key).type(type).group(group).build();
        when(cardRepository.findAll()).thenReturn(Lists.newArrayList(card));

        List<Card> cards = cardService.getAllCards();
        assertNotNull(cards);
        assertThat(cards, hasSize(1));
        assertThat(cards.iterator().next().getName(), equalTo(name));

    }

    @Test
    public void shouldGetCardByName() {
        Card card = Card.builder().id(1l).name(name).type(type).group(group).build();
        card.initEntity();
        when(cardRepository.findByName(anyString())).thenReturn(card);

        Card cardFind = cardService.getCardByName(name);
        assertNotNull(cardFind);
        assertNotNull(cardFind.getPk());
        assertNotNull(cardFind.getCreatedAt());
        assertThat(cardFind.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldSaveCardWhenCardValid() {
        Card card = Card.builder().id(1l).name(name).type(type).group(group).build();
        card.initEntity();
        when(cardRepository.saveOrUpdateCard((Card) any())).thenReturn(card);

        Card savedCard = cardService.saveCard(card);
        assertNotNull(savedCard);
        assertNotNull(savedCard.getCreatedAt());
        assertThat(savedCard.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertNotNull(savedCard.getId());
    }

    @Test
    public void shouldReturnExceptionWhenCardInvalid() {
        doThrow(ValidatorException.class).when(cardRepository).saveOrUpdateCard((Card) any());

        rule.expect(ValidatorException.class);

        cardService.saveCard(new Card());
    }

    @Test
    public void shouldReturnExceptionWhenCardNull() {
        doThrow(RepositoryException.class).when(cardRepository).saveOrUpdateCard((Card) any());

        rule.expect(RepositoryException.class);

        cardService.saveCard(null);
    }

}