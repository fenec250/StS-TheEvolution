package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

public class Evolution
		extends CustomCard {
	public static final String ID = "evolutionmod:Evolution";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
	private static final int COST = 0;
	private static final int GENE_AMT = 2;
//	private static final int UPGRADE_GENE_AMT = 1;

	private ArrayList<AbstractGene> genes;

	public Evolution() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.BASIC, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = GENE_AMT;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		genes.forEach(gene -> AbstractDungeon.actionManager.addToBottom(new ChannelAction(gene.makeCopy())));
	}

	@Override
	public void triggerWhenDrawn() {
		super.triggerWhenDrawn();
		this.genes = new ArrayList<AbstractGene>();
		ArrayList<AbstractGene> genesPool = new ArrayList<>();
		genesPool.add(new CentaurGene());
		genesPool.add(new PlantGene());
		genesPool.add(new MerfolkGene());
		genesPool.add(new HarpyGene());
		genesPool.add(new LavafolkGene());
		genesPool.add(new SuccubusGene());
		genesPool.add(new LymeanGene());
		genesPool.add(new InsectGene());
		if (this.upgraded) {
			Set<String> exclude =
			AbstractDungeon.player.orbs.stream()
					.map(o -> o.ID)
					.collect(Collectors.toSet());
			genesPool.removeIf(gene -> exclude.contains(gene.ID));
		}
		StringBuilder description = new StringBuilder(DESCRIPTION + " NL ");

		for (int i = 0; i < this.magicNumber; ++i) {
			AbstractGene gene = genesPool.get(AbstractDungeon.cardRng.random(genesPool.size() - 1));
			genes.add(gene);
			description.append(gene.coloredName(false)).append(" ");
		}
		this.rawDescription = description.toString();
		initializeDescription();
	}

	@Override
	public AbstractCard makeCopy() {
		return new Evolution();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
//			this.upgradeMagicNumber(UPGRADE_GENE_AMT);
		}
	}
}