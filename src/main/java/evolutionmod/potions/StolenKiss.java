package evolutionmod.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import evolutionmod.powers.LustPower;

public class StolenKiss extends AbstractPotion {


	public static final String POTION_ID = "evolutionmod:StolenKiss";
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public StolenKiss() {
		this(POTION_ID);
	}

	public StolenKiss(String ID) {
		// The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
		super(NAME, ID, PotionRarity.UNCOMMON, PotionSize.HEART, PotionColor.FIRE);

		// Do you throw this potion at an enemy or do you just consume it.
		this.isThrown = true;
		this.targetRequired = true;
	}

	@Override
	public void use(AbstractCreature target) {
		addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new LustPower(target, getPotency()), getPotency()));
	}

	@Override
	public void initializeData() {
		potency = getPotency();
		// Initialize the Description
		description = DESCRIPTIONS[0] + getPotency() + DESCRIPTIONS[1];
		// Initialize the on-hover name + description
		tips.clear();
		tips.add(new PowerTip(name, description));
		super.initializeData();
	}

	@Override
	public AbstractPotion makeCopy() {
		return new StolenKiss();
	}

	@Override
	public int getPotency(final int ascensionLevel) {
		return 7;
	}
}