package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.BramblesPower;
import evolutionmod.powers.PotencyPower;

import java.util.List;

public class PlantGene extends AbstractGene {
	public static final String ID = "evolutionmod:PlantGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR = "[#60B040]";
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/orbs/PlantGene.png";
	public static final int BLOCK = 1;
	public static final int BRAMBLE = 3;

	public PlantGene() {
		super(ID, NAME, getDescription(), IMG_PATH, COLOR);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new PlantGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		int thorns = bramble() * times;
		int block = block();
		if (block > 0) {
			for (int i = 0; i < times; ++i) {
				AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
			}
		}
		AbstractDungeon.actionManager.addToTop(
				new ApplyPowerAction(p, p, new BramblesPower(p, thorns), thorns));
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
		if (rawDescription.contains("Plant")) {
			tooltips.add(new TooltipInfo(
					COLOR + NAME + "[]",
					getDescription()));
		}
		return tooltips;
	}

	public static String getDescription() {
		return DESCRIPTION[0] + block() + DESCRIPTION[1] + bramble() + DESCRIPTION[2];
	}

	private static int block() {
		int block = BLOCK;
		if (CardCrawlGame.isInARun()) {
			if (AbstractDungeon.player.hasPower(PotencyPower.POWER_ID)) {
				block += AbstractDungeon.player.getPower(PotencyPower.POWER_ID).amount;
			}
		}
		return block > 0 ? block : 0;
	}

	private static int bramble() {
		return BRAMBLE;
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
			PlantGene.apply(p, m, this.amount);
		}

		@Override
		public String text() {
			return "[#60B040]Plant[]";
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
