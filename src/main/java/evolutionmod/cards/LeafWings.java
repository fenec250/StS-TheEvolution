package evolutionmod.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.orbs.HarpyGene2;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.GrowthPower;

import java.util.Iterator;

public class LeafWings extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:LeafWings";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LeafWings.png";
    private static final int COST = 1;
    private static final int POWER_AMT = 2;
    private static final int UPGRADE_POWER_AMT = 1;
    private static final int GROWTH_AMT = 1;
    private static final int UPGRADE_GROWTH_AMT = 2;

    public LeafWings() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = POWER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new LeafWingsPower(p, this.magicNumber)));
        formEffect(HarpyGene2.ID, ()-> addToBot(
                new ApplyPowerAction(p, p, new GrowthPower(p, this.upgraded ? UPGRADE_GROWTH_AMT : GROWTH_AMT))
        ));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LeafWings();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_POWER_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

	@Override
	public void triggerOnGlowCheck() {
		super.triggerOnGlowCheck();
		if (isPlayerInThisForm(HarpyGene2.ID)) {
			this.glowColor = HarpyGene2.COLOR;
		} else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
	}


    public static class LeafWingsPower extends TwoAmountPower {
        public static final String POWER_ID = "evolutionmod:LeafWingsPower";
        public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String NAME = cardStrings.NAME;
        public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

        public LeafWingsPower(AbstractCreature owner, int initialAmount) {
            this.name = NAME;
            this.ID = POWER_ID;
            this.owner = owner;
            this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/SymbiotesPower84.png"), 0, 0, 84, 84);
            this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/SymbiotesPower32.png"), 0, 0, 32, 32);
            this.type = PowerType.BUFF;
            this.amount = initialAmount;
            this.updateDescription();
            this.amount2 = initialAmount;
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            super.onApplyPower(power, target, source);
            if (power.ID.equals(GrowthPower.POWER_ID)) {
                int total = power.amount;
                if (target.hasPower(GrowthPower.POWER_ID)) {
                    total += target.getPower(GrowthPower.POWER_ID).amount;
                }
                int draws = Math.min(total/GrowthPower.ENERGY_THRESHOLD, this.amount2);
                this.amount2 -= draws;
                if (draws > 0) {
                    this.flash();
                    addToBot(new DrawCardAction(this.owner, draws));
                }
            }
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + (this.amount2) + DESCRIPTIONS[2];
        }

        public void stackPower(int stackAmount) {
            this.fontScale = 8.0F;
            this.amount += stackAmount;
            if (this.amount <= 0) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
            this.amount2 += stackAmount;
            if (this.amount2 <= 0) {
                this.amount2 = 0; // should never happen anyway, only if some custom enemy/card reduces arbitrary powers <<wince>>
            }
        }

        public void atStartOfTurn() {
            this.amount2 = this.amount;
        }
    }
}