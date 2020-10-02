package evolutionmod.orbs;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.actions.SuccubusGeneAction;
import evolutionmod.cards.AdaptableEvoCard;
import evolutionmod.powers.PotencyPower;

import java.util.List;

public class SuccubusGene extends AbstractGene {
	public static final String ID = "evolutionmod:SuccubusGene";
	public static final OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(ID);
	public static final String NAME = orbStrings.NAME;
	public static final String COLOR = "[#F04040]";
	public static final String[] DESCRIPTION = orbStrings.DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
	public static final int DAMAGE = 1;
	public static final int VULNERABLE = 2;

	public SuccubusGene() {
		super(ID, NAME, getDescription(), IMG_PATH, COLOR);
	}

	@Override
	public void onStartOfTurn() {
		super.onStartOfTurn();
		apply(AbstractDungeon.player, null, 1);
	}

	@Override
	public AbstractOrb makeCopy() {
		return new SuccubusGene();
	}

	@Override
	public void playChannelSFX() {
	}

	public static void apply(AbstractPlayer p, AbstractMonster m, int times) {
		int damage = damage();
		int vulnerable = vulnerable();
		for (int i = 0; i < times; ++i) {
			AbstractDungeon.actionManager.addToBottom(new SuccubusGeneAction(p, m, damage, vulnerable));
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
		if (rawDescription.contains("Succubus")) {
			tooltips.add(new TooltipInfo(
					COLOR + NAME + "[]",
					getDescription()));
		}
		return tooltips;
	}

	public static String getDescription() {
		return DESCRIPTION[0] + damage() + DESCRIPTION[1] + vulnerable() + DESCRIPTION[1];
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
			SuccubusGene.apply(p, m, this.amount);
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
