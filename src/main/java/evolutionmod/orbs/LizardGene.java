package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.LizardGeneAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.PotencyPower;
import evolutionmod.powers.SalamanderPower;

import java.util.List;

public class LizardGene extends AbstractGene {
	public static final String ID = "evolutionmod:LizardGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR = "[#80E080]";
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/LizardGene.png";
	public static final int DAMAGE = 0;
	public static final int POISON = 2;

	public LizardGene() {
		super(ID, NAME, getDescription(), IMG_PATH, COLOR);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new LizardGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		for (int i = 0; i < times; ++i) {
			AbstractDungeon.actionManager.addToBottom(new LizardGeneAction(
					p, m, damage() * times, poison() * times));
		}
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = getOrbDescription();
	}

	public static String getOrbDescription() {
		return "At the #bstart #bof #byour #bturn and when #yEvoked: NL " + getDescription();
	}
/*- Dark Conviction does not stack correctly, with two in play I still get only 1 Strength per card exhausted.
- Dark Conviction does not deal the correct amount of damage. If you have 0 strength when it procs it does not deal the damage for the strength it gives you. If you do have Strength the damage it deals is affected by Attack modifiers like Weak, Scorch and Strength again.
- Flame Ward + adds 3 Burns but indicates only 2.
- At the start of your turn you draw a card for each Status in your hand. This is still not explained anywhere. This makes the fight against Sentries much easier.
- Ashfall shows a value increased by Strength in the description. The buff gained is not affected and the damage dealt neither.
- Holy Barrier gains +4 temp hp on upgrade, that's a lot for a 1-cost uncommon skill.
- Holy Shock gains +2 damage on upgrade, that's very low for a 2-cost uncommon attack.
- Forbidden Magics does deal a lot of damage. I instantly won against a Looter thanks to Forbidden Magics+ (42 dmg) and Bag of Marbles (2 dmg). It also saved me some Burn damage a few times.
- */
	public static String getDescription() {
		return (damage() <= 0
				? DESCRIPTION[0] + poison() + DESCRIPTION[1] + DESCRIPTION[4]
				: DESCRIPTION[0] + poison() + DESCRIPTION[1] + DESCRIPTION[2] + damage() + DESCRIPTION[3] + DESCRIPTION[4]);
//		return DESCRIPTION[0] + damage() + DESCRIPTION[1] + poison() + DESCRIPTION[2];
	}

	private static int damage() {
		int damage = DAMAGE;
		damage += SalamanderPower.getLizardDamage();
		return damage;
	}

	private static int poison() {
		return POISON;
	}

	public static class Adaptation extends AdaptableEvoCard.AbstractAdaptation {

		public Adaptation(int amount) {
			super(amount);
		}
		public Adaptation(int amount, int max) {
			super(amount, max);
		}
		Adaptation(Adaptation adaptation) {
			super(adaptation);
		}

		@Override
		public void apply(AbstractPlayer p, AbstractMonster m) {
			LizardGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Lizard";
		}

		@Override
		public String getGeneId() {
			return ID;
		}

		@Override
		public AdaptableEvoCard.AbstractAdaptation makeCopy() {
			return new Adaptation(this);
		}
	}
}
