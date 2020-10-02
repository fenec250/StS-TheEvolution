package evolutionmod.orbs;

import basemod.abstracts.CustomOrb;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import evolutionmod.cards.AdaptableEvoCard;

import java.util.List;

public final class GeneUtils {

//	public static String getColoredName() {
//		return colorize(this.name);
//	}
//	public static String colorize(String string) {
//		return colorString + string + "[]";
//	}
//
//	public static boolean isPlayerInThisForm() {
//		return AbstractDungeon.player.orbs.stream()
//				.anyMatch((orb) -> (orb.getClass() == THIS.class));
//	}

	public static boolean isPlayerInThisForm(String orbId) {
		return AbstractDungeon.player.orbs.stream()
				.anyMatch((orb) -> orb != null && orb.ID != null && orb.ID.equals(orbId));
	}

	public static List<TooltipInfo> addTooltip(List<TooltipInfo> tooltips, String rawDescription) {
		if (rawDescription.contains("Lavafolk")) {
			tooltips.add(new TooltipInfo(
					LavafolkGene.COLOR + LavafolkGene.NAME + "[]",
					LavafolkGene.getDescription()));
		}
		if (rawDescription.contains("Plant")) {
			tooltips.add(new TooltipInfo(
					PlantGene.COLOR + PlantGene.NAME + "[]",
					PlantGene.getDescription()));
		}
		return tooltips;
	}
}
