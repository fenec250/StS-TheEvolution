package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.BeastGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.HarpyGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.orbs.MerfolkGene;
import evolutionmod.orbs.PlantGene;
import evolutionmod.orbs.ShadowGene;
import evolutionmod.orbs.SuccubusGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mutate extends BaseEvoCard {
	public static final String ID = "evolutionmod:Mutate";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/Mutate.png";
	private static final int COST = 0;
	private static final int GENE_CHOICES = 3;

	public Mutate() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = GENE_CHOICES;
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {

		this.addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				List<String> current = p.orbs.stream()
						.filter(o -> o != null && o.ID != null)
						.map(o -> o.ID)
						.collect(Collectors.toList());
				List<String> available = new ArrayList<>();
				if (!current.contains(PlantGene.ID)) { available.add(PlantGene.ID);}
				if (!current.contains(MerfolkGene.ID)) { available.add(MerfolkGene.ID);}
				if (!current.contains(HarpyGene.ID)) { available.add(HarpyGene.ID);}
				if (!current.contains(LavafolkGene.ID)) { available.add(LavafolkGene.ID);}
				if (!current.contains(SuccubusGene.ID)) { available.add(SuccubusGene.ID);}
				if (!current.contains(LymeanGene.ID)) { available.add(LymeanGene.ID);}
				if (!current.contains(InsectGene.ID)) { available.add(InsectGene.ID);}
				if (!current.contains(BeastGene.ID)) { available.add(BeastGene.ID);}
				if (!current.contains(LizardGene.ID)) { available.add(LizardGene.ID);}
				if (!current.contains(CentaurGene.ID)) { available.add(CentaurGene.ID);}
				if (!current.contains(ShadowGene.ID)) { available.add(ShadowGene.ID);}

				if (available.size() == 1) {
					addToTop(new ChannelAction(getGene(available.get(0))));
				} else {
					while (available.size() > magicNumber) {
						available.remove(AbstractDungeon.cardRng.random(available.size() - 1));
					}
					ArrayList<AbstractCard> cardChoices = new ArrayList<>(available.stream()
							.map(o -> {
								Gene gene = new Gene(getGene(o));
								if (upgraded) {gene.upgrade();}
								return gene;
							})
							.collect(Collectors.toList()));
					addToTop(new ChooseOneAction(cardChoices));
				}
				this.isDone = true;
			}
		});
	}

	@Override
	public AbstractCard makeCopy() {
		return new Mutate();
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.exhaust = false;
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}