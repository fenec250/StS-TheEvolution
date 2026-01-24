package evolutionmod.cardsV1;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.cards.GlowingCard;
import evolutionmod.orbsV1.BeastGene;
import evolutionmod.orbsV1.ShadowGene;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.ShadowsPower;

import java.util.List;

public class BlackCat
        extends BaseEvoCard implements GlowingCard {
    public static final String cardID = "BlackCat";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/BlackCat.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int UPGRADE_DAMAGE_AMT = 3;
    private static final int FORM_DAMAGE_AMT = 3;

    public BlackCat() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = FORM_DAMAGE_AMT;
        this.isMultiDamage = true;
//        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, this.multiDamage,
                this.damageTypeForTurn,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        BaseEvoCard.formEffect(BeastGene.ID);

        BaseEvoCard.formEffect(ShadowGene.ID, () -> addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ShadowsPower.triggerImmediately(p);
                this.isDone = true;
            }
        }));
    }

    @Override
    public void applyPowers() {
        alterDamageAround(super::applyPowers);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        alterDamageAround(() -> super.calculateCardDamage(mo));
    }

    private void alterDamageAround(Runnable supercall) {
        this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT : 0);
        if (isPlayerInTheseForms(BeastGene.ID)) {
            this.baseDamage += this.magicNumber;
        }
        supercall.run();
        this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT : 0);
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackCat();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

    @Override
    public int getNumberOfGlows() {
        return 2;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return true;
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        switch (glowIndex) {
            case 0:
                return isPlayerInThisForm(BeastGene.ID) ? BeastGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            case 1:
                return isPlayerInThisForm(ShadowGene.ID, BeastGene.ID) ? ShadowGene.COLOR.cpy()
                        : BLUE_BORDER_GLOW_COLOR.cpy();
            default:
                return BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (customTooltips == null) {
            super.getCustomTooltips();
            customTooltips.add(new TooltipInfo(EXTENDED_DESCRIPTION[0],
                    EXTENDED_DESCRIPTION[1]));
        }
        return  customTooltips;
    }
}
