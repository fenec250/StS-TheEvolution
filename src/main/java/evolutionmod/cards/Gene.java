package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.*;
import evolutionmod.orbsV1.*;
import evolutionmod.patches.EvolutionEnum;

public class Gene extends BaseEvoCard {
	public static final String ID = "evolutionmodV2:Gene";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalDust.png";
	private static final int COST = -2;

	private final AbstractGene gene;

	public Gene(AbstractGene gene) {
		this(gene, EXTENDED_DESCRIPTION);
	}

	public Gene(AbstractGene gene, String[] descriptions) {
		super(ID, NAME, getImgPath(gene.ID), COST, DESCRIPTION,
				CardType.SKILL, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.gene = gene;
		String coloredGene = replaceGeneIds(gene.ID);
		this.name = coloredGene + descriptions[0];
		this.rawDescription = descriptions[1] + coloredGene + descriptions[2];
		initializeDescription();
	}

	public AbstractGene getGene() {
		return gene;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		addToBot(((AbstractGene) this.gene.makeCopy()).getChannelAction());
		if (upgraded) {
			addToBot(new AbstractGameAction() {
				@Override
				public void update() {
					gene.getAdaptation().apply(p, m);
					this.isDone = true;
				}
			});
		}
	}

	@Override
	public void onChoseThisOption() {
		addToBot(((AbstractGene) this.gene.makeCopy()).getChannelAction());
		if (upgraded) {
			addToBot(new AbstractGameAction() {
				@Override
				public void update() {
					gene.getAdaptation().apply(AbstractDungeon.player, null);
					this.isDone = true;
				}
			});
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Gene(this.gene);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = EXTENDED_DESCRIPTION[1] + replaceGeneIds(gene.ID)
					+ EXTENDED_DESCRIPTION[3] + this.gene.description.replace("#y","").replace("#b","");
			this.initializeDescription();
		}
	}

	private static String getImgPath(String geneId) {
		String path = "evolutionmod/images/cards/";
		if (PlantGene2.ID.equals(geneId)) { path += "Plant";}
		else if (MerfolkGene2.ID.equals(geneId)) { path += "Merfolk";}
		else if (HarpyGene2.ID.equals(geneId)) { path += "Harpy";}
		else if (LavafolkGene2.ID.equals(geneId)) { path += "Lavafolk";}
		else if (SuccubusGene2.ID.equals(geneId)) { path += "Succubus";}
		else if (LymeanGene2.ID.equals(geneId)) { path += "Lymean";}
		else if (InsectGene2.ID.equals(geneId)) { path += "Insect";}
		else if (BeastGene2.ID.equals(geneId)) { path += "Beast";}
		else if (LizardGene2.ID.equals(geneId)) { path += "Lizard";}
		else if (CentaurGene2.ID.equals(geneId)) { path += "Centaur";}
		else if (ShadowGene2.ID.equals(geneId)) { path += "Shadow";}

		else if (PlantGene.ID.equals(geneId)) { path += "Plant";}
		else if (MerfolkGene.ID.equals(geneId)) { path += "Merfolk";}
		else if (HarpyGene.ID.equals(geneId)) { path += "Harpy";}
		else if (LavafolkGene.ID.equals(geneId)) { path += "Lavafolk";}
		else if (SuccubusGene.ID.equals(geneId)) { path += "Succubus";}
		else if (LymeanGene.ID.equals(geneId)) { path += "Lymean";}
		else if (InsectGene.ID.equals(geneId)) { path += "Insect";}
		else if (BeastGene.ID.equals(geneId)) { path += "Beast";}
		else if (LizardGene.ID.equals(geneId)) { path += "Lizard";}
		else if (CentaurGene.ID.equals(geneId)) { path += "Centaur";}
		else if (ShadowGene.ID.equals(geneId)) { path += "Shadow";}
		else { return "evolutionmod/images/cards/CrystalDust.png";}
		return path + "Skl.png";
	}
}