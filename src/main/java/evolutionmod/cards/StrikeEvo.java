package evolutionmod.cards;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.AdaptationPower;

import java.util.stream.Collectors;

public class StrikeEvo
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:StrikeEvo";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 6;
    private static final int UPGRADE_DAMAGE_AMT = 3;

    public StrikeEvo() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.BASIC, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.tags.add(BaseModCardTags.BASIC_STRIKE);
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        if (p.hasPower(AdaptationPower.POWER_ID)) {
            addToTop(new ApplyPowerAction(p, p, new AdaptationPower(p, -1), -1));
            p.orbs.stream()
                    .filter(o -> this.canAdaptWith(o) > 0)
                    .findAny()
                    .ifPresent(o -> this.tryAdaptingWith(o, true));
        }
        this.useAdaptations(p, m);
    }

    @Override
    public int canAdaptWith(AbstractAdaptation adaptation) {
        return adaptation.amount;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }
}