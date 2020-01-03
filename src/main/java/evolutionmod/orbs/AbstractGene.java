package evolutionmod.orbs;

import basemod.abstracts.CustomOrb;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.cards.AdaptableEvoCard;

public abstract class AbstractGene extends CustomOrb {
//	public static final String ID = "evolutionmod:Strike_Melody";
//	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
//	public static final String NAME = cardStrings.NAME;
//	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
//	public static final String EVOKE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
//	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";

	public AbstractGene(String id, String name, String description, String imgPath) {
		super(id, name, 1, 1, description, description, imgPath);
	}

	@Override
	public void onEvoke() {
//		if (AbstractDungeon.player.hasPower("TODO Tanid form")) {
//			this.apply(AbstractDungeon.player, null, AbstractDungeon.player.getPower("TODO Tanid form").amount);
//		}
	}

	public abstract AdaptableEvoCard.AbstractAdaptation getAdaptation();
	public String coloredName(boolean plural) {
		return "#y" + this.name;
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
