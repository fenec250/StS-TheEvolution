package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import evolutionmod.orbs.GhostGene;
import evolutionmod.orbs.LymeanGeneV2;
import evolutionmod.patches.AbstractCardEnum;

import java.util.Optional;
import java.util.stream.Collectors;

public class Blessing
        extends CustomCard {
    public static final String ID = "evolutionmod:Blessing";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/strike.png";
    private static final int COST = 1;
    private static final int EXHAUST_AMT = 1;
    private static final int ARTIFACT_AMT = 1;

    public Blessing() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = ARTIFACT_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        p.drawPile.group.stream()
                .filter(card -> card.type == CardType.CURSE)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> list.size() > 0
                                ? Optional.of(list.get(AbstractDungeon.cardRng.random(list.size() - 1)))
                                : Optional.empty()))
                .ifPresent(card -> addToBot(new ExhaustSpecificCardAction(((AbstractCard) card), p.drawPile)));

        addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ChannelAction(new LymeanGeneV2()));
        addToBot(new ChannelAction(new GhostGene()));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}