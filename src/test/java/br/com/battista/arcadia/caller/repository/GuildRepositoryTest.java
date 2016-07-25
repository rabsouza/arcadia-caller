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
import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.BaseEntity;
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.Guild;
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.model.HeroGuild;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.model.enuns.GroupCardEnum;
import br.com.battista.arcadia.caller.model.enuns.NameGuildEnum;
import br.com.battista.arcadia.caller.model.enuns.TypeCardEnum;
import br.com.battista.arcadia.caller.validator.EntityValidator;

@RunWith(MockitoJUnitRunner.class)
public class GuildRepositoryTest extends BaseRepositoryConfig {

    private final String name = "guild01";
    private final String key = "key01";
    private final NameGuildEnum nameGuild = NameGuildEnum.BLUE;

    private final String username = "abc0_";
    private final String mail = "abc@abc.com";
    private final ProfileAppConstant profile = ProfileAppConstant.APP;
    private final TypeCardEnum type = TypeCardEnum.UPGRADE;
    private final GroupCardEnum group = GroupCardEnum.STARTING_EQUIPMENT;
    private final int defense = 2;
    private final int life = 4;
    @Rule
    public ExpectedException rule = ExpectedException.none();
    private User user;
    private Card card;
    private Hero hero;
    private HeroGuild heroGuild1;
    private HeroGuild heroGuild2;
    private HeroGuild heroGuild3;
    @InjectMocks
    private GuildRepository guildRepository;

    @Mock
    private EntityValidator entityValidator;

    @Before
    public void setup() {
        user = User.builder().username(username).mail(mail).profile(profile).build();
        objectifyRepository.save().entity(user).now();

        card = Card.builder().name(name).key(key).type(type).group(group).build();
        objectifyRepository.save().entity(card).now();

        hero = Hero.builder().name(name).defense(defense).life(life).build();
        objectifyRepository.save().entity(hero).now();

        heroGuild1 = HeroGuild.builder().hero(hero).card1(card).build();
        heroGuild2 = HeroGuild.builder().hero(hero).card1(card).build();
        heroGuild3 = HeroGuild.builder().hero(hero).card1(card).build();
    }

    @Test
    public void shouldEmptyGuildsWhenEmptyDataBase() {
        List<Guild> guilds = guildRepository.findAll();
        assertNotNull(guilds);
        assertThat(guilds, hasSize(0));
    }

    @Test
    public void shouldReturnGuildsWhenFindAllGuilds() {

        Guild guild = Guild.builder().user(user).name(nameGuild).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();
        objectifyRepository.save().entity(guild).now();

        List<Guild> guilds = guildRepository.findAll();
        assertNotNull(guilds);
        assertThat(guilds, hasSize(1));
        assertThat(guilds.iterator().next().getName(), equalTo(nameGuild));
    }

    @Test
    public void shouldSaveGuildWhenValidGuild() {
        Guild guild = Guild.builder().user(user).name(nameGuild).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();

        Guild savedGuild = guildRepository.saveOrUpdateGuild(guild);
        assertNotNull(savedGuild);
        assertNotNull(savedGuild.getPk());
        assertNotNull(savedGuild.getCreatedAt());
        assertThat(savedGuild.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertThat(savedGuild.getName(), equalTo(nameGuild));
        assertThat(savedGuild.getUser(), equalTo(user));
        assertThat(savedGuild.getHero01(), equalTo(heroGuild1));
        assertThat(savedGuild.getHero02(), equalTo(heroGuild2));
        assertThat(savedGuild.getHero03(), equalTo(heroGuild3));
    }

    @Test
    public void shouldFindByNameWhenValidGuildAndValidName() {
        Guild guild = Guild.builder().user(user).name(nameGuild).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();

        Guild savedGuild = guildRepository.saveOrUpdateGuild(guild);
        assertNotNull(savedGuild);
        assertNotNull(savedGuild.getPk());
        assertNotNull(savedGuild.getCreatedAt());
        assertThat(savedGuild.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Guild guildFind = guildRepository.findByName(guild.getName().name());
        assertNotNull(guildFind);
        assertThat(guildFind.getPk(), equalTo(savedGuild.getPk()));
        assertThat(guildFind.getVersion(), equalTo(savedGuild.getVersion()));
        assertThat(guildFind.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertThat(guildFind.getName(), equalTo(savedGuild.getName()));
        assertThat(guildFind.getUser(), equalTo(savedGuild.getUser()));
        assertThat(guildFind.getHero01(), equalTo(savedGuild.getHero01()));
        assertThat(guildFind.getHero02(), equalTo(savedGuild.getHero02()));
        assertThat(guildFind.getHero03(), equalTo(savedGuild.getHero03()));
    }

    @Test
    public void shouldReturnNullWhenFindByNameWithValidGuildAndInvalidName() {
        Guild guild = Guild.builder().user(user).name(nameGuild).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();

        Guild savedGuild = guildRepository.saveOrUpdateGuild(guild);
        assertNotNull(savedGuild);
        assertNotNull(savedGuild.getPk());
        assertNotNull(savedGuild.getCreatedAt());
        assertThat(savedGuild.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Guild guildFind = guildRepository.findByName("abcd");
        assertNull(guildFind);
    }

    @Test
    public void shouldFindByMailWhenValidGuildAndValidMail() {
        Guild guild = Guild.builder().user(user).name(nameGuild).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();

        Guild savedGuild = guildRepository.saveOrUpdateGuild(guild);
        assertNotNull(savedGuild);
        assertNotNull(savedGuild.getPk());
        assertNotNull(savedGuild.getCreatedAt());
        assertThat(savedGuild.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Guild guildFind = guildRepository.findByMail(guild.getUser().getMail());
        assertNotNull(guildFind);
        assertThat(guildFind.getPk(), equalTo(savedGuild.getPk()));
        assertThat(guildFind.getVersion(), equalTo(savedGuild.getVersion()));
        assertThat(guildFind.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertThat(guildFind.getName(), equalTo(savedGuild.getName()));
        assertThat(guildFind.getUser(), equalTo(savedGuild.getUser()));
        assertThat(guildFind.getHero01(), equalTo(savedGuild.getHero01()));
        assertThat(guildFind.getHero02(), equalTo(savedGuild.getHero02()));
        assertThat(guildFind.getHero03(), equalTo(savedGuild.getHero03()));
    }

    @Test
    public void shouldReturnNullWhenFindByMailWithValidGuildAndInvalidMail() {
        Guild guild = Guild.builder().user(user).name(nameGuild).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();

        Guild savedGuild = guildRepository.saveOrUpdateGuild(guild);
        assertNotNull(savedGuild);
        assertNotNull(savedGuild.getPk());
        assertNotNull(savedGuild.getCreatedAt());
        assertThat(savedGuild.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));

        Guild guildFind = guildRepository.findByMail("abcd");
        assertNull(guildFind);
    }

    @Test
    public void shouldThrowExceptionWhenSaveGuildWithInvalidName() {
        Guild guild = Guild.builder().user(user).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();

        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        guildRepository.saveOrUpdateGuild(guild);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithNullGuild() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        guildRepository.findByName(null);
    }

    @Test
    public void shouldThrowExceptionWhenFindByNameWithEmptyGuild() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        guildRepository.findByName("");
    }

    @Test
    public void shouldThrowExceptionWhenFindByMailWithNullGuild() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        guildRepository.findByMail(null);
    }

    @Test
    public void shouldThrowExceptionWhenFindByMailWithEmptyGuild() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        guildRepository.findByMail("");
    }

    @Test
    public void shouldThrowExceptionWhenSaveNullGuild() {
        rule.expect(RepositoryException.class);
        rule.expectMessage(containsString("not be null!"));

        guildRepository.saveOrUpdateGuild(null);
    }

    @Test
    public void shouldThrowExceptionWhenSaveInvalidGuild() {
        doThrow(ValidatorException.class).when(entityValidator).validate((BaseEntity) anyObject());

        rule.expect(ValidatorException.class);

        guildRepository.saveOrUpdateGuild(new Guild());
    }

}