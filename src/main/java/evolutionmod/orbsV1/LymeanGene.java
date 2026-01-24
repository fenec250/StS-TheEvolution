package evolutionmod.orbsV1;

import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.FateAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.orbs.AbstractGene;

public class LymeanGene extends AbstractGene {
	public static final String ID = "evolutionmod:LymeanGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR_STRING = "[#50FFFF]";
	public static final Color COLOR = new Color(0x80DFFF00);
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final TooltipInfo TOOLTIP = new TooltipInfo(COLOR_STRING + NAME + "[]", "Orb: " + getOrbDescription());
	public static final String IMG_PATH = "evolutionmod/images/orbs/LymeanGene.png";
	public static final int SCRY = 2;

	public LymeanGene() {
		super(ID, NAME, getDescription(), IMG_PATH);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1, false);
	}

	@Override
	public void onEvoke() {
//		super.onEvoke();
		apply(AbstractDungeon.player, null, 1, true);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new LymeanGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times, boolean addToTop) {
		int fate = fate();
		if (fate > 0) {
			for (int i = 0; i < times; ++i) {
				if (addToTop) {
					AbstractDungeon.actionManager.addToTop(new FateAction(fate));
				} else {
					AbstractDungeon.actionManager.addToBottom(new FateAction(fate));
				}
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
//		return DESCRIPTION[0] + blockPerGene() + DESCRIPTION[1] + healPerGene() + DESCRIPTION[2];
		return DESCRIPTION[0] + fate() + DESCRIPTION[1];
	}

	private static int fate() {
		return SCRY;
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
			LymeanGene.apply(p, m, this.amount, true);
		}

		@Override
		public String text() {
			return "Lymean";
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
