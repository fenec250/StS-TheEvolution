package evolutionmod.orbs;

import basemod.abstracts.CustomOrb;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.AdaptableEvoCard;

public abstract class AbstractGene extends CustomOrb {

	private String colorString;
	public AbstractGene(String id, String name, String description, String imgPath, String color) {
		super(id, name, 1, 1, description, description, imgPath);
		this.colorString = color;
	}

	@Override
	public void onEvoke() {
		onStartOfTurn();
		onEndOfTurn();
	}

	public abstract AdaptableEvoCard.AbstractAdaptation getAdaptation();
	public String getColoredName() {
		return colorize(this.name);
	}
	public String colorize(String string) {
		return colorString + string + "[]";
	}
//
//	public static boolean isPlayerInThisForm() {
//		return AbstractDungeon.player.orbs.stream()
//				.anyMatch((orb) -> (orb.getClass() == THIS.class));
//	}

	public static boolean isPlayerInThisForm(String orbId) {
		return AbstractDungeon.player.orbs.stream()
				.anyMatch((orb) -> orb != null && orb.ID != null && orb.ID.equals(orbId));
	}

//	public abstract class GeneAdaptation {
//		public GeneAdaptation(String geneId, ) {
//
//		}
//
//		public abstract void apply(AbstractPlayer p, AbstractMonster m, int times);
//
//	}
}
