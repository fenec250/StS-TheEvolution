package evolutionmod.orbs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.LizardGeneAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.SalamanderPower;

public class LizardGene extends AbstractGene {
	public static final String ID = "evolutionmod:LizardGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR_STRING = "[#80E080]";
	public static final Color COLOR = new Color(0x80E08000);
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/LizardGene.png";
	public static final int DAMAGE = 0;
	public static final int POISON = 2;

	public LizardGene() {
		super(ID, NAME, getDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public void onEvoke() {
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

	public static String getDescription() {
		return (damage() <= 0
				? DESCRIPTION[0] + poison() + DESCRIPTION[1] + DESCRIPTION[4]
				: DESCRIPTION[0] + poison() + DESCRIPTION[1] + DESCRIPTION[2] + damage() + DESCRIPTION[3] + DESCRIPTION[4]);
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
