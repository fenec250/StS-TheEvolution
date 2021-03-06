package evolutionmod.potions;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import evolutionmod.actions.MutagenAction;

public class Mutagen extends AbstractPotion {


	public static final String POTION_ID = "evolutionmod:Mutagen";
	private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

	public Mutagen() {
		// The bottle shape and inside is determined by potion size and color. The actual colors are the main DefaultMod.java
		super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.S, PotionColor.POISON);

		// Potency is the damage/magic number equivalent of potions.
//		potency = getPotency();

//		description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];

		// Do you throw this potion at an enemy or do you just consume it.
		isThrown = false;
	}

	@Override
	public void use(AbstractCreature target) {
		addToBot(new MutagenAction(getPotency()));
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
		return new Mutagen();
	}

	@Override
	public int getPotency(final int ascensionLevel) {
		return 2;
	}
}