package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.patches.AbstractCardEnum;

public class CursedTouch
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:CursedTouch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CursedTouch.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int UPGRADE_DAMAGE_AMT = 2;
    private static final int WEAK_AMT = 1;
    private static final int UPGRADE_WEAK_AMT = 1;
//    private static final int MAX_ADAPT_AMT = 2;
//    private static final int UPGRADE_MAX_ADAPT_AMT = 1;

    public CursedTouch() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = WEAK_AMT;
//        this.adaptationMap.put(ShadowGene.ID, new ShadowGene.Adaptation(0, WEAK_AMT));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
//        super.use(p, m);
//        BaseEvoCard.formEffect(ShadowGene.ID, () ->
//                p.orbs.stream()
//                    .filter(o -> this.canAdaptWith(o) > 0)
//                    .findAny()
//                    .ifPresent(o -> this.tryAdaptingWith(o, true)));
        formEffect(ShadowGene.ID, () -> this.adapt(1));
        this.useAdaptations(p, m);
    }

    @Override
    protected int canAdaptWith(AbstractAdaptation adaptation) {
        return ShadowGene.ID.equals(adaptation.getGeneId()) ? adaptation.amount : 0;
    }

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
//            this.upgradeAdaptationMaximum(ShadowGene.ID, UPGRADE_MAX_ADAPT_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(ShadowGene.ID)) {
            this.glowColor = ShadowGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }
}