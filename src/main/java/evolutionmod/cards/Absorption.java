package evolutionmod.cards;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.patches.EvolutionEnum;

public class Absorption
		extends BaseEvoCard {
	public static final String ID = "evolutionmodV2:Absorption";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/Absorption.png";
	private static final int COST = 1;
	private static final int POWER_AMT = 2;
	private static final int UPGRADE_POWER_AMT = 1;

	public Absorption() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.POWER, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = POWER_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		this.addToBot(new ApplyPowerAction(
				p, p, new AbsorptionPower(p, this.magicNumber)));
	}

	@Override
	public void onChoseThisOption() {
		addToBot(new MakeTempCardInHandAction(this, false));
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_POWER_AMT);
		}
	}

	public static class AbsorptionPower extends TwoAmountPower {
		public static final String POWER_ID = "evolutionmod:AbsorptionPower1";
		public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
		public static final String NAME = cardStrings.NAME;
		public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

		public AbsorptionPower(AbstractCreature owner, int initialAmount) {
			this.name = NAME;
			this.ID = POWER_ID;
			this.owner = owner;
//        this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/lava power 84.png"), 0, 0, 84, 84);
//        this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/lava power 32.png"), 0, 0, 32, 32);
			this.loadRegion("focus");
			this.type = PowerType.BUFF;
			this.amount = initialAmount;
			this.amount2 = initialAmount;
			updateDescription();
		}

		@Override
		public void updateDescription() {
			description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.amount2 + DESCRIPTIONS[2];
		}

		@Override
		public void onEvokeOrb(AbstractOrb orb) {
			if (this.amount2 > 0 && orb instanceof AbstractGene) {
				orb.onStartOfTurn();
				orb.onEndOfTurn();
				this.amount2 -= 1;
				this.flash();
			}
			super.onEvokeOrb(orb);
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

		@Override
		public void atStartOfTurn() {
			this.amount2 = this.amount;
		}
	}
}