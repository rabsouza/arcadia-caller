package br.com.battista.arcadia.caller.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.appengine.repackaged.com.google.common.collect.Lists;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.GroupCardEnum;
import br.com.battista.arcadia.caller.model.TypeCardEnum;
import br.com.battista.arcadia.caller.repository.CardRepository;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {

    private final String name = "card01";
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
        Card card = Card.builder().name(name).type(type).group(group).build();
        when(cardRepository.findAll()).thenReturn(Lists.newArrayList(card));

        List<Card> cards = cardService.getAllCards();
        assertNotNull(cards);
        assertThat(cards, hasSize(1));
        assertThat(cards.iterator().next().getName(), equalTo(name));

    }

    public void shouldGetCardByName() {
        Card card = Card.builder().id(1l).name(name).type(type).group(group).build();
        card.initEntity();
        when(cardRepository.findByName(Matchers.anyString())).thenReturn(card);

        Card cardMail = cardService.getCardByName(name);
        assertNotNull(cardMail);
        assertNotNull(cardMail.getPk());
        assertNotNull(cardMail.getCreatedAt());
        assertThat(cardMail.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldSaveCardWhenCardValid() {
        Card card = Card.builder().id(1l).name(name).type(type).group(group).build();
        card.initEntity();
        when(cardRepository.saveOrUpdateCard((Card) Matchers.any())).thenReturn(card);

        Card savedCard = cardService.saveCard(card);
        assertNotNull(savedCard);
        assertNotNull(savedCard.getCreatedAt());
        assertThat(savedCard.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertNotNull(savedCard.getId());
    }

    @Test
    public void shouldReturnExceptionWhenCardInvalid() {
        doThrow(ValidatorException.class).when(cardRepository).saveOrUpdateCard((Card) Matchers.any());

        rule.expect(ValidatorException.class);

        cardService.saveCard(new Card());
    }

    @Test
    public void shouldReturnExceptionWhenCardNull() {
        doThrow(RepositoryException.class).when(cardRepository).saveOrUpdateCard((Card) Matchers.any());

        rule.expect(RepositoryException.class);

        cardService.saveCard(null);
    }

}