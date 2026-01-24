package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.*;
import evolutionmod.patches.EvolutionEnum;
import evolutionmod.powers.MasteryPower;

import java.util.List;
import java.util.Optional;

public class Mastery
		extends BaseEvoCard implements CustomSavable<String> {
	public static final String ID = "evolutionmodV2:Mastery";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	public static final String IMG_PATH = "evolutionmod/images/cards/GeneMastery.png";
	private static final int COST = 1;
	private static final int FORM_TRIGGER = 1;

	private AbstractGene gene;

	public Mastery() {
		this(null);
	}

	private Mastery(AbstractGene gene) {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
				CardType.POWER, EvolutionEnum.EVOLUTION_V2_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.magicNumber = this.baseMagicNumber = FORM_TRIGGER;
		this.gene = gene;
		resetGene();
	}

	@Override
	public boolean canPlay(AbstractCard card) {
		if (card == this && gene == null
				&& AbstractDungeon.player.orbs.stream()
						.noneMatch(o -> o instanceof AbstractGene)) {
			this.cantUseMessage = EXTENDED_DESCRIPTION[6];
			return false;
		}
		return super.canPlay(card);
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (gene == null) {
			Optional<AbstractOrb> nextGene = AbstractDungeon.player.orbs.stream()
					.filter(o -> o instanceof AbstractGene)
					.findFirst();
			if (nextGene.isPresent()) {
				gene = ((AbstractGene) nextGene.get());
				p.masterDeck.group.stream()
						.filter(c -> c.uuid.equals(this.uuid)).findAny()
						.ifPresent(c -> {
							((Mastery) c).gene = this.gene;
							((Mastery) c).resetGene();
						});
				resetGene();
			}
			else {
				return;
			}
		}
		addToBot(new ApplyPowerAction(p, p, new MasteryPower(p, this.magicNumber, gene.ID)));
		addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				if (upgraded) {
					addToTop(((AbstractGene)gene.makeCopy()).getChannelAction());
					addToTop(new IncreaseMaxOrbAction(1));
				} else {
					formEffect(gene.ID, () -> addToTop(new IncreaseMaxOrbAction(1)));
				}
				this.isDone = true;
			}
		});
	}

	@Override
	public void onChoseThisOption() {
		addToBot(new MakeTempCardInHandAction(this, false));
	}

	@Override
	public AbstractCard makeCopy() {
		return this.gene == null ? new Mastery() : new Mastery(this.gene);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
			this.resetGene();
//			this.upgradeBaseCost(UPGRADED_COST);
		}
	}

	@Override
	public void triggerOnGlowCheck() {
		String geneId = "";
		if (gene != null && !upgraded && isPlayerInThisForm(gene.ID)) {
			geneId = gene.ID;
		} else if (gene == null) {
			Optional<AbstractOrb> nextGene = AbstractDungeon.player.orbs.stream()
					.filter(o -> o instanceof AbstractGene)
					.findFirst();
			if(nextGene.isPresent()) {
				geneId = nextGene.get().ID;
			}
		}
		switch (geneId) {
			case HarpyGene2.ID: this.glowColor = HarpyGene2.COLOR.cpy(); return;
			case MerfolkGene2.ID: this.glowColor = MerfolkGene2.COLOR.cpy(); return;
			case LavafolkGene2.ID: this.glowColor = LavafolkGene2.COLOR.cpy(); return;
			case CentaurGene2.ID: this.glowColor = CentaurGene2.COLOR.cpy(); return;
			case LizardGene2.ID: this.glowColor = LizardGene2.COLOR.cpy(); return;
			case BeastGene2.ID: this.glowColor = BeastGene2.COLOR.cpy(); return;
			case PlantGene2.ID: this.glowColor = PlantGene2.COLOR.cpy(); return;
			case ShadowGene2.ID: this.glowColor = ShadowGene2.COLOR.cpy(); return;
			case LymeanGene2.ID: this.glowColor = LymeanGene2.COLOR.cpy(); return;
			case InsectGene2.ID: this.glowColor = InsectGene2.COLOR.cpy(); return;
			case SuccubusGene2.ID: this.glowColor = SuccubusGene2.COLOR.cpy(); return;
			default: this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy(); return;
		}
	}

	@Override
	public List<TooltipInfo> getCustomTooltips() {
		if (customTooltips == null) {
			super.getCustomTooltips();
			customTooltips.add(new TooltipInfo("Imprinted Form",
					"The #yForm on this card is selected the first time it is played, using the oldest channeled Gene."));
		}
		return customTooltips;
	}

	private void resetGene() {
		if (gene != null) {
			this.rawDescription = EXTENDED_DESCRIPTION[0]
					+ gene.ID + EXTENDED_DESCRIPTION[1]
					+ (!this.upgraded
					? gene.ID + EXTENDED_DESCRIPTION[2]
					: EXTENDED_DESCRIPTION[3] + gene.ID + EXTENDED_DESCRIPTION[4]);
			this.name = replaceGeneIds(this.gene.ID + EXTENDED_DESCRIPTION[5] + (upgraded ? "+" : ""));
		} else {
			this.name = NAME + (upgraded ? "+" : "");
			this.rawDescription = this.upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION;
		}
		initializeDescription();
		initializeTitle();
	}

	@Override
	public String onSave() {
		return this.gene != null ? gene.ID : "";
	}

	@Override
	public void onLoad(String geneId) {
		this.gene = getGene(geneId);
		resetGene();
	}
}