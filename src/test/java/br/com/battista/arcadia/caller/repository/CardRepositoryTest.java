package br.com.battista.arcadia.caller.repository;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.battista.arcadia.caller.constants.EntityConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.enuns.GroupCardEnum;
import br.com.battista.arcadia.caller.model.enuns.TypeCardEnum;
import br.com.battista.arcadia.caller.validator.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class CardRepositoryTest extends BaseRepositoryConfig {

    private final String name = "card01";
    private final TypeCardEnum type = TypeCardEnum.UPGRADE;
    private final GroupCardEnum group = GroupCardEnum.NONE;

    @Rule
    public ExpectedException rule = ExpectedException.none();

    @InjectMocks
    private CardRepository cardRepository;

    @Mock
    private EntityValidator entityValidator;

    @Test
    public void shouldEmptyCardsWhenEmptyDataBase() {
        List<Card> cards = cardRepository.findAll();
        assertNotNull(cards);
        assertThat(cards, hasSize(0));
    }

    @Test
    public void shouldReturnCardsWhenFindAllCards() {
        Card card = Card.builder().name(name).type(type).group(group).build();
        objectifyRepository.save().entity(card).now();

        List<Card> cards = cardRepository.findAll();
        assertNotNull(cards);
        assertThat(cards, hasSize(1));
        assertThat(cards.iterator().next().getName(), equalTo(name));
    }

    @Test
    public void shouldSaveCardWhenValidCard() {
        Card card = Card.builder().name(name).type(type).group(group).build();

        Card savedCard = cardRepository.saveOrUpdateCard(card);
        assertNotNull(savedCard);
        assertNotNull(savedCard.getPk());
        assertNotNull(savedCard.getCreatedAt());
        assertThat(savedCard.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldFindByNameWhenValidCardAndValidName() {
        Card card = Card.builder().name(name).type(type).group(group).build();

        Card savedCard = cardRepository.saveOrUpdateCard(card);
        assertNotNull(savedCard);
        assertNotNull(savedCard.getPk());
        assertNotNull(savedCard.getCreatedAt());
        assertThat(savedCard.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Card cardFind = cardRepository.findByName(card.getName());
        assertNotNull(cardFind);
        assertThat(cardFind.getPk(), equalTo(savedCard.getPk()));
        assertThat(cardFind.getVersion(), equalTo(savedCard.getVersion()));
        assertThat(cardFind.getName(), equalTo(savedCard.getName()));
    }

    @Test
    public void shouldReturnNullWhenFindByNameWithValidCardAndInvalidName() {
        Card card = Card.builder().name(name).type(type).group(group).build();

        Card savedCard = cardRepository.saveOrUpdateCard(card);
        assertNotNull(savedCard);
        assertNotNull(savedCard.getPk());
        assertNotNull(savedCard.getCreatedAt());
        assertThat(savedCard.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Card cardFind = cardRepository.findByName("abcd");
        assertNull(cardFind);
    }

    @Test
    public void shouldThrowExceptionWhenSaveCardWithInvalidName() {
        Card card = Card.builder().name("abc").type(type).group(group).build();

        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        cardRepository.saveOrUpdateCard(card);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithNullCard() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        cardRepository.findByName(null);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithEmptyCard() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        cardRepository.findByName("");
    }

    @Test
    public void shouldThrowExceptionWhenSaveNullCard() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        cardRepository.saveOrUpdateCard(null);
    }

    @Test
    public void shouldThrowExceptionWhenSaveInvalidCard() {
        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        cardRepository.saveOrUpdateCard(new Card());
    }

}