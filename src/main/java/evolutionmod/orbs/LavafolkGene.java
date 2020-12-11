package evolutionmod.orbs;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.LavafolkGeneAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.SalamanderPower;

public class LavafolkGene extends AbstractGene {
	public static final String ID = "evolutionmod:LavafolkGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR = "[#FF9050]";
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/LavafolkGene.png";
	public static final int DAMAGE = 2;
	public static final int STRIKE_NB = 2;

	public LavafolkGene() {
		super(ID, NAME, getDescription(), IMG_PATH, COLOR);
	}

	@Override
	public void onEndOfTurn() {
		super.onEndOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new LavafolkGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		int damage = damage();
		if (damage > 0) {
			for (int i = 0; i < strikeNb() * times; ++i) {
				AbstractDungeon.actionManager.addToBottom(new LavafolkGeneAction(p, m, damage));
			}
		}
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	public static AdaptableEvoCard.AbstractAdaptation getAdaptation(int amount, int maximum) {
		return new Adaptation(amount, maximum);
	}

	@Override
	public void updateDescription() {
//		super.updateDescription();
		this.description = "#yEnd #yof #yTurn and #yEvoke: " + getDescription();
	}

	public static String getDescription() {
		int strikes = strikeNb();
		return DESCRIPTION[0] + damage() + DESCRIPTION[1] + (strikes > 1 ? " " + strikes + DESCRIPTION[2] + DESCRIPTION[3] : DESCRIPTION[3]);
	}

	private static int damage() {
		int damage = DAMAGE;
		damage += SalamanderPower.getLavafolkExtraDamage();
		return damage;
	}

	private static int strikeNb() {
		return STRIKE_NB;
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
			LavafolkGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Lavafolk";
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
