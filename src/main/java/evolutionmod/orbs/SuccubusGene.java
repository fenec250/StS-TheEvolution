package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.SuccubusGeneAction;
import evolutionmod.cards.AdaptableEvoCard;

public class SuccubusGene extends AbstractGene {
	public static final String ID = "evolutionmod:SuccubusGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR_STRING = "[#F04040]";
	public static final Color COLOR = new Color(0xF0404000);
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final TooltipInfo TOOLTIP = new TooltipInfo(COLOR_STRING + NAME + "[]", "Orb: " + getOrbDescription());
	public static final String IMG_PATH = "evolutionmod/images/orbs/SuccubusGene.png";
	public static final int LUST = 2;
	public static final int VULNERABLE = 0;

	public SuccubusGene() {
		super(ID, NAME, getDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1, false);
	}

	@Override
	public void onEvoke() {
		apply(AbstractDungeon.player, null, 1, true);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new SuccubusGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times, boolean addToTop) {
		int damage = damage();
		int vulnerable = vulnerable();
		for (int i = 0; i < times; ++i) {
			if (addToTop) {
				AbstractDungeon.actionManager.addToTop(new SuccubusGeneAction(p, m, damage, vulnerable));
			} else {
				AbstractDungeon.actionManager.addToBottom(new SuccubusGeneAction(p, m, damage, vulnerable));
			}
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
		return DESCRIPTION[0] + damage() + DESCRIPTION[1];
	}

	private static int damage() {
		int damage = LUST;
		return damage > 0 ? damage : 0;
	}

	private static int vulnerable() {
		return VULNERABLE;
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
			SuccubusGene.apply(p, m, this.amount, true);
		}

		@Override
		public String text() {
			return "[#F04040]Succubus[]";
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
