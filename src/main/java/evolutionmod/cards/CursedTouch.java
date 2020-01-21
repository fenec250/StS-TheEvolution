package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.GhostGene;
import evolutionmod.patches.AbstractCardEnum;

public class CursedTouch
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:CursedTouch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int WEAK_AMT = 2;
    private static final int UPGRADE_WEAK_AMT = 1;
    private static final int MAX_ADAPT_AMT = 2;
    private static final int UPGRADE_MAX_ADAPT_AMT = 1;

    public CursedTouch() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.COMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = WEAK_AMT;
        this.adaptationMap.put(GhostGene.ID, new GhostGene.Adaptation(0, WEAK_AMT));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        AbstractDungeon.actionManager.addToTop(
                new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
//        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new GhostGene()));
//        if (this.canAdaptWith())
//        this.maxAdaptationMap.put(GholdstGene.ID, MAX_ADAPT_AMT);
        p.orbs.stream()
                .filter(o -> this.canAdaptWith(o) > 0)
                .findAny()
                .ifPresent(o -> this.tryAdaptingWith((AbstractGene) o, true));
//        this.upgradeAdaptationMaximum(GholdstGene.ID, UPGRADE_MAX_ADAPT_AMT);
        this.useAdaptations(p, m);
    }

//    @Override
//    public int addAdaptation(AbstractGene gene) {
//        if (!gene.ID.equals(GhostGene.ID)) {
//            return 0;
//        }
//        if (this.adaptationMap.containsKey(GhostGene.ID)
//                && this.adaptationMaximum <= this.adaptationMap.get(GhostGene.ID).amount) {
//            return 0;
//        }
//        return super.addAdaptation(gene);
//    }

    @Override
    public AbstractCard makeCopy() {
        return new CursedTouch();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeMagicNumber(UPGRADE_WEAK_AMT);
            this.upgradeAdaptationMaximum(GhostGene.ID, UPGRADE_MAX_ADAPT_AMT);
        }
    }
}