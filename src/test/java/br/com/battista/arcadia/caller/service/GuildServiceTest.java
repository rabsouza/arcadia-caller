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
import br.com.battista.arcadia.caller.constants.ProfileAppConstant;
import br.com.battista.arcadia.caller.exception.RepositoryException;
import br.com.battista.arcadia.caller.exception.ValidatorException;
import br.com.battista.arcadia.caller.model.Card;
import br.com.battista.arcadia.caller.model.Guild;
import br.com.battista.arcadia.caller.model.Hero;
import br.com.battista.arcadia.caller.model.HeroGuild;
import br.com.battista.arcadia.caller.model.User;
import br.com.battista.arcadia.caller.model.enuns.GroupCardEnum;
import br.com.battista.arcadia.caller.model.enuns.NameGuildEnum;
import br.com.battista.arcadia.caller.model.enuns.TypeCardEnum;
import br.com.battista.arcadia.caller.repository.GuildRepository;

@RunWith(MockitoJUnitRunner.class)
public class GuildServiceTest {

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
    private GuildService guildService;

    @Mock
    private GuildRepository guildRepository;

    @Before
    public void setup() {
        user = User.builder().username(username).mail(mail).profile(profile).build();

        card = Card.builder().name(name).key(key).type(type).group(group).build();

        hero = Hero.builder().name(name).defense(defense).life(life).build();

        heroGuild1 = HeroGuild.builder().hero(hero).card1(card).build();
        heroGuild2 = HeroGuild.builder().hero(hero).card1(card).build();
        heroGuild3 = HeroGuild.builder().hero(hero).card1(card).build();
    }

    @Test
    public void shouldGetAllGuilds() {
        Guild guild = Guild.builder().user(user).name(nameGuild).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();
        when(guildRepository.findAll()).thenReturn(Lists.newArrayList(guild));

        List<Guild> guilds = guildService.getAllGuilds();
        assertNotNull(guilds);
        assertThat(guilds, hasSize(1));
        assertThat(guilds.iterator().next().getName(), equalTo(nameGuild));

    }

    @Test
    public void shouldGetGuildByName() {
        Guild guild = Guild.builder().id(1l).user(user).name(nameGuild).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();
        guild.initEntity();
        when(guildRepository.findByName(anyString())).thenReturn(guild);

        Guild guildFind = guildService.getGuildByName(name);
        assertNotNull(guildFind);
        assertNotNull(guildFind.getPk());
        assertNotNull(guildFind.getCreatedAt());
        assertThat(guildFind.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldGetGuildByMail() {
        Guild guild = Guild.builder().id(1l).user(user).name(nameGuild).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();
        guild.initEntity();
        when(guildRepository.findByMail(anyString())).thenReturn(guild);

        Guild guildFind = guildService.getGuildByMail(guild.getUser().getMail());
        assertNotNull(guildFind);
        assertNotNull(guildFind.getPk());
        assertNotNull(guildFind.getCreatedAt());
        assertThat(guildFind.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
    }

    @Test
    public void shouldSaveGuildWhenGuildValid() {
        Guild guild = Guild.builder().id(1l).user(user).name(nameGuild).hero01(heroGuild1).hero02(heroGuild2).hero03(heroGuild3).build();
        guild.initEntity();
        when(guildRepository.saveOrUpdateGuild((Guild) any())).thenReturn(guild);

        Guild savedGuild = guildService.saveGuild(guild);
        assertNotNull(savedGuild);
        assertNotNull(savedGuild.getCreatedAt());
        assertThat(savedGuild.getVersion(), equalTo(EntityConstant.DEFAULT_VERSION));
        assertNotNull(savedGuild.getId());
    }

    @Test
    public void shouldReturnExceptionWhenGuildInvalid() {
        doThrow(ValidatorException.class).when(guildRepository).saveOrUpdateGuild((Guild) any());

        rule.expect(ValidatorException.class);

        guildService.saveGuild(new Guild());
    }

    @Test
    public void shouldReturnExceptionWhenGuildNull() {
        doThrow(RepositoryException.class).when(guildRepository).saveOrUpdateGuild((Guild) any());

        rule.expect(RepositoryException.class);

        guildService.saveGuild(null);
    }

}