package evolutionmod.cards;

import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import evolutionmod.orbs.*;
import evolutionmod.patches.AbstractCardEnum;

import java.util.List;
import java.util.Optional;

public class GeneBlast
		extends BaseEvoCard implements CustomSavable<String>, GlowingCard {
	public static final String ID = "evolutionmod:GeneBlast";
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
//	public static final String IMG_PATH = "evolutionmod/images/cards/CrystalDust.png";
	private static final int COST = 1;
	private static final int DAMAGE_AMT = 8;
	private static final int GENE_DAMAGE_AMT = 4;
	private static final int UPGRADE_DAMAGE_AMT = 3;

	private AbstractGene gene;

	public GeneBlast() {
		super(ID, NAME, new RegionName("purple/attack/bowling_bash"), COST, DESCRIPTION,
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.UNCOMMON, CardTarget.SELF);
		this.baseDamage = this.damage = DAMAGE_AMT;
		this.baseMagicNumber = this.magicNumber = GENE_DAMAGE_AMT;
		resetGene();
	}

	private GeneBlast(AbstractGene gene) {
		super(ID, NAME, new RegionName("purple/attack/bowling_bash"), COST, DESCRIPTION,
				CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
				CardRarity.COMMON, CardTarget.ENEMY);
		this.baseDamage = this.damage = DAMAGE_AMT;
		this.baseMagicNumber = this.magicNumber = GENE_DAMAGE_AMT;
		this.gene = gene;
		resetGene();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (gene == null || isPlayerInThisForm(gene.ID)) {
			Optional<AbstractOrb> nextGene = AbstractDungeon.player.orbs.stream()
					.filter(o -> o instanceof AbstractGene)
					.findFirst();
			if (nextGene.isPresent()) {
				addToBot(new WaitAction(0.4F));
				gene = ((AbstractGene) nextGene.get());
				p.masterDeck.group.stream()
						.filter(c -> c.uuid.equals(this.uuid)).findAny()
						.ifPresent(c -> {
							((GeneBlast) c).gene = this.gene;
							((GeneBlast) c).resetGene();
						});
				resetGene();
			}
//			else {
//				return;
//			}
		}
		addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.FIRE));
		if (gene != null) {
			formEffect(gene.ID);
		}
	}

	@Override
	public void applyPowers() {
		alterDamageAround(super::applyPowers);
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		alterDamageAround(() -> super.calculateCardDamage(mo));
	}

	private void alterDamageAround(Runnable supercall) {
		this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT : 0);
		if ((this.gene != null && BaseEvoCard.isPlayerInThisForm(this.gene.ID))
		|| this.gene == null && AbstractDungeon.player.orbs.stream().anyMatch(o -> o instanceof AbstractGene)) {
			this.baseDamage += this.magicNumber;
		}
		supercall.run();
		this.baseDamage = DAMAGE_AMT + (upgraded ? UPGRADE_DAMAGE_AMT : 0);
		this.isDamageModified = this.damage != this.baseDamage;
	}

	@Override
	public AbstractCard makeCopy() {
		return new GeneBlast(this.gene);
	}

	@Override
	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_DAMAGE_AMT);
		}
	}

	@Override
	public int getNumberOfGlows() {
		return 2;
	}

	@Override
	public boolean isGlowing(int glowIndex) {
		return true;
	}

	@Override
	public Color getGlowColor(int glowIndex) {
		switch (glowIndex) {
			case 0:
				return gene != null && isPlayerInThisForm(gene.ID) ? getGeneColor(gene.ID)
						: BLUE_BORDER_GLOW_COLOR.cpy();
			case 1:
				return gene != null && isPlayerInThisForm(gene.ID)
						? AbstractDungeon.player.orbs.stream()
							.filter(o -> o instanceof AbstractGene)
							.findFirst()
							.map(g -> getGeneColor(g.ID))
							.orElse(BLUE_BORDER_GLOW_COLOR.cpy())
						: BLUE_BORDER_GLOW_COLOR.cpy();
			default:
				return BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
	private Color getGeneColor(String geneId) {
		switch (geneId) {
			case HarpyGene.ID: return HarpyGene.COLOR.cpy();
			case MerfolkGene.ID: return MerfolkGene.COLOR.cpy();
			case LavafolkGene.ID: return LavafolkGene.COLOR.cpy();
			case CentaurGene.ID: return CentaurGene.COLOR.cpy();
			case LizardGene.ID: return LizardGene.COLOR.cpy();
			case BeastGene.ID: return BeastGene.COLOR.cpy();
			case PlantGene.ID: return PlantGene.COLOR.cpy();
			case ShadowGene2.ID: return ShadowGene2.COLOR.cpy();
			case LymeanGene.ID: return LymeanGene.COLOR.cpy();
			case InsectGene.ID: return InsectGene.COLOR.cpy();
			case SuccubusGene.ID: return SuccubusGene.COLOR.cpy();
			default: return BLUE_BORDER_GLOW_COLOR.cpy();
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
		if (!CardCrawlGame.isInARun()) {
			return;
		}
		if (gene != null) {
			this.name = replaceGeneIds(this.gene.ID + EXTENDED_DESCRIPTION[2]);
			this.rawDescription = gene == null ? DESCRIPTION : EXTENDED_DESCRIPTION[0]
					+ gene.ID + EXTENDED_DESCRIPTION[1];
		} else {
			this.name = NAME;
			this.rawDescription = DESCRIPTION;
		}
		initializeDescription();
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