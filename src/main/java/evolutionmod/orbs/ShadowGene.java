package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.ShadowGeneAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.PotencyPower;

import java.util.List;

public class ShadowGene extends AbstractGene {
	public static final String ID = "evolutionmod:ShadowGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR = "[#8060A0]";
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/ShadowGene.png";
	public static final int DAMAGE = 1;
	public static final int WEAK = 2;

	public ShadowGene() {
		super(ID, NAME, getDescription(), IMG_PATH, COLOR);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new ShadowGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
//		int damage = damage();
		int weak = weak();
		for (int i = 0; i < times; ++i) {
			AbstractDungeon.actionManager.addToBottom(new ShadowGeneAction(p, m, weak));
		}
	}

	@Override
	public AdaptableEvoCard.AbstractAdaptation getAdaptation() {
		return new Adaptation(1);
	}

	@Override
	public void updateDescription() {
		this.description = "#yPassive and #yEvoke: " + getDescription();
	}

	public static List<TooltipInfo> addTooltip(List<TooltipInfo> tooltips, String rawDescription) {
		if (rawDescription.contains("Shadow")) {
			tooltips.add(new TooltipInfo(
					COLOR + NAME + "[]",
					getDescription()));
		}
		return tooltips;
	}

	public static String getDescription() {
		return DESCRIPTION[0] + weak() + DESCRIPTION[1];
//		return DESCRIPTION[0] + damage() + DESCRIPTION[1] + weak() + DESCRIPTION[2];
	}

	private static int damage() {
		int damage = DAMAGE;
		if (CardCrawlGame.isInARun()) {
			if (AbstractDungeon.player.hasPower(PotencyPower.POWER_ID)) {
				damage += AbstractDungeon.player.getPower(PotencyPower.POWER_ID).amount;
			}
		}
		return damage > 0 ? damage : 0;
	}
	private static int weak() {
		return WEAK;
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
			ShadowGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "Ghost";
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

	public static boolean IsPlayerInThisForm() {
		return AbstractDungeon.player.orbs.stream()
				.anyMatch((orb) -> (orb.getClass() == BeastGene.class));
	}
}
